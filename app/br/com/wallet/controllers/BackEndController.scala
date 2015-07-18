package br.com.wallet.controllers

import java.util.UUID

import br.com.wallet.api.controller.ActionController
import br.com.wallet.api.models.result.Success
import br.com.wallet.types.loginOption.LoginOption
import br.com.wallet.types.loginTypes.{GithubType, LoginType, GoogleType}
import br.com.wallet.types.oauthUser.OAuthUser
import play.api.libs.json.Json
import play.api.{Configuration, Play}
import play.api.http.HeaderNames
import play.api.libs.ws.WS
import play.api.mvc.Action
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current
import scala.language.postfixOps

import scala.concurrent.Future

/**
 * Created by sirkleber on 29/06/15.
 */
object BackEndController extends ActionController {

  private lazy val loginList: List[LoginType] = {
    lazy val conf: Configuration = Play.current.configuration
    List(GoogleType(conf), GithubType(conf))
  }

  def auth = Action.async { implicit req =>
    lazy val callbackUrl = routes.BackEndController.login _

    def list(state: String): List[Option[LoginOption]] = for {
      ltype <- loginList
    } yield ltype.authData((state, callbackUrl))

    Future {
      req.session.get(oauthStateSesion) match {
        case Some(token) => Ok (Success(list(token)) toJson) as jsonApp
        case None =>
          lazy val state: String = UUID.randomUUID().toString
          Ok (Success(list(state)) toJson).withSession(oauthStateSesion-> state) as jsonApp
      }
    }
  }

  def login(provider: String, codeOpt: Option[String], stateOpt: Option[String]) = Action.async { implicit req =>
    (for {
      loginType <- loginList find (_.provider == provider)
      code <- codeOpt
      state <- stateOpt
      oauthState <- req.session.get(oauthStateSesion)
    } yield if(state == oauthState) {
        lazy val callbackUrl = routes.BackEndController.login(loginType.provider, None, None).absoluteURL()
        for {
          fToken <- loginType.getToken(code, callbackUrl)
        } yield (for {
          token <- fToken
        } yield {
            def json = Json.toJson(OAuthUser("", state, code, token, loginType))
            Redirect("/spreadsheet").withSession(oauthUserSession -> json.toString)
          }
        ).getOrElse(BadRequest("Provedor não autorizou"))
      } else {
        Future.successful(BadRequest("Você não está logado"))
      }
    ) getOrElse Future.successful(BadRequest("Servidor não proveu os valores"))
  }

  def success = Action.async { request =>
    request.session.get(oauthStateSesion).fold(Future.successful(Unauthorized("No way Jose"))) { authToken =>
      WS.url("https://api.github.com/user/repos").
        withHeaders(HeaderNames.AUTHORIZATION -> s"token $authToken").
        get().map { response =>
        Ok(response.json)
      }
    }
  }

}