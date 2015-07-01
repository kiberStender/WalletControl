package br.com.wallet.controllers

import java.util.UUID

import br.com.wallet.api.controller.ActionController
import br.com.wallet.api.models.result.{Success}
import br.com.wallet.api.oAuth.OAuth2Solver
import br.com.wallet.types.loginTypes.{GitHub, LoginTypes, Google}
import play.api.{Configuration, Play}
import play.api.http.HeaderNames
import play.api.libs.ws.WS
import play.api.mvc.{RequestHeader, Action}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current
import scala.language.postfixOps

import scala.concurrent.Future

/**
 * Created by sirkleber on 29/06/15.
 */
object BackEndController extends ActionController {

  private def loginList(implicit req: RequestHeader): List[LoginTypes] = {
    lazy val callbackUrl = routes.BackEndController.auth_(None, None).absoluteURL()
    lazy val conf: Configuration = Play.current.configuration

    List(Google(conf, callbackUrl), GitHub(conf, callbackUrl))
  }

  def auth = Action.async { implicit req =>
    Future {
      req.session.get("oauth-state") match {
        case Some(token) => Ok(Success(token) toJson) as jsonApp
        case None =>
          lazy val state = UUID.randomUUID().toString
          Ok (
            Success(for {
              ltype <- loginList
              url <- ltype.url
            } yield s"$url&state=$state") toJson
          ).withSession("oauth-state"-> state) as jsonApp
      }
    }
  }

  def auth_(codeOpt: Option[String], stateOpt: Option[String]) = OAuth2Solver.callbackUrl(codeOpt, stateOpt)

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
