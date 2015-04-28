package controllers

import java.util.UUID

import br.com.wallet.api.actors.AuthActor
import br.com.wallet.api.controller.ActionController
import br.com.wallet.api.oAuth.OAuth2Solver
import play.api.http.HeaderNames
import play.api.libs.ws.WS
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor._
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

object Application extends ActionController {
  private implicit val timeout = Timeout(3 second)
  //private def jsonApp = "application/json"
  //private def hash = "usuario"
  private lazy val auth = Akka.system.actorOf(Props[AuthActor])

  def index = Action.async { implicit req =>
    Future {
      req.session.get("oauth-state") match {
        case Some(_) => Ok(views.html.index("Your new application is ready."))
        case None =>
          def callbackUrl = routes.Application.auth_(None, None).absoluteURL()
          lazy val state = UUID.randomUUID().toString

          OAuth2Solver.getAuthorizationUrl(callbackUrl)("repo")(state) match {
            case Some(url) => Redirect(url).withSession("oauth-state" -> state)
            case None => Forbidden("AuthKey was not provided")
          }
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

  def getAllItens = Action.async { implicit req =>
    req.session.get("oauth-state") match {
      case Some(_) => Future(Ok("Kiber"))
      case None => Future.successful(Redirect(routes.Application.index))
    }
  }
}