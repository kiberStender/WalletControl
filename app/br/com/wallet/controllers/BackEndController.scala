package br.com.wallet.controllers

import java.util.UUID

import br.com.wallet.api.controller.ActionController
import br.com.wallet.api.models.result.{Success}
import br.com.wallet.types.loginOption.LoginOption
import br.com.wallet.types.loginTypes.{GitHub, LoginTypes, Google}
import play.api.{Configuration, Play}
import play.api.http.{MimeTypes, HeaderNames}
import play.api.libs.ws.WS
import play.api.mvc.{Results, Action}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current
import scala.language.postfixOps

import scala.concurrent.Future

/**
 * Created by sirkleber on 29/06/15.
 */
object BackEndController extends ActionController {
  private implicit lazy val conf: Configuration = Play.current.configuration

  private lazy val loginList: Map[String, LoginTypes] = {
    lazy val callbackUrl = routes.BackEndController.login _

    Map("google" -> Google(conf, callbackUrl), "github" -> GitHub(conf, callbackUrl))
  }

  def auth = Action.async { implicit req =>
    def list: String => List[Option[LoginOption]] = state => for {
      (_, ltype) <- loginList toList
    } yield ltype authData state

    Future {
      req.session.get("oauth-state") match {
        case Some(token) => Ok (Success(list(token)) toJson) as jsonApp
        case None =>
          lazy val state: String = UUID.randomUUID().toString
          Ok (Success(list(state)) toJson).withSession("oauth-state"-> state) as jsonApp
      }
    }
  }

  private def getToken: String => Future[Option[String]] = code => (for {
    authSec <- conf.getString("github.client.secret")
    authId <- conf.getString("github.client.id")
  } yield {
      def tokenResponse = WS.url("https://github.com/login/oauth/access_token").
        withQueryString("client_id" -> authId, "client_secret" -> authSec, "code" -> code).
        withHeaders(HeaderNames.ACCEPT -> MimeTypes.JSON).
        post(Results.EmptyContent())

      for {
        response <- tokenResponse
      } yield (response.json \ "access_token").asOpt[String]
    }).getOrElse(Future(None))

  def login(provider: String, codeOpt: Option[String], stateOpt: Option[String]) = Action.async { implicit req =>
    Future {
      Ok(
        (for {
          code <- codeOpt
          state <- stateOpt
        } yield s"$provider -> $code -> $state").getOrElse("Isaew")
      )
    }
  }

  def auth_(codeOpt: Option[String], stateOpt: Option[String]) = Action.async { implicit req =>
    Future {
      (for {
        code <- codeOpt
        state <- stateOpt
        oauthState <- req.session.get("oauth-state")
      } yield if (oauthState == state) {
          for {
            accessToken <- getToken(code)
          } yield Ok("Você está logado")
        } else {
          Future.successful(BadRequest("Você não está logado"))
        }
      ).getOrElse(Future.successful(BadRequest("Servidor não proveu os valores")))

      Ok("")
    }
  }

  def success = Action.async { request =>
    request.session.get("oauth-token").fold(Future.successful(Unauthorized("No way Jose"))) { authToken =>
      WS.url("https://api.github.com/user/repos").
        withHeaders(HeaderNames.AUTHORIZATION -> s"token $authToken").
        get().map { response =>
        Ok(response.json)
      }
    }
  }

}