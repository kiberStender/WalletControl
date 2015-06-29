package br.com.wallet.controllers

import java.util.UUID

import br.com.wallet.api.controller.ActionController
import br.com.wallet.api.models.result.{Failure, Success}
import br.com.wallet.api.oAuth.OAuth2Solver
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
object BackEndController extends ActionController{

  def auth = Action.async { implicit req =>
    Future {
      def result: Either[Option[(String, String)], String] = req.session.get("oauth-state") match {
        case Some(token) => Right(token)
        case None =>
          def callbackUrl = routes.BackEndController.auth_(None, None).absoluteURL()
          lazy val state = UUID.randomUUID().toString

          Left {
            OAuth2Solver.getAuthorizationUrl(callbackUrl)("repo")(state) match {
              case Some(url) => Some((url, state))
              case None => None
            }
          }
      }

      (result match {
        case Right(token) => Ok(Success(token) toJson)
        case Left(Some((url, state))) => Ok(Failure(url) toJson) withSession "oauth-state"-> state
        case Left(None) => Ok(Failure("AuthKey was not provided") toJson)
      }) as jsonApp
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
