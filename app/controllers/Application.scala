package controllers

import java.util.UUID

import br.com.wallet.api.controller.ActionController
import br.com.wallet.api.oAuth.OAuth2Solver
import play.api.http.HeaderNames
import play.api.libs.ws.WS
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor._
import scala.concurrent.{Promise, Future}
import scala.language.postfixOps

object Application extends ActionController {
  //private lazy val auth = Akka.system.actorOf(Props[AuthActor])

  def index = Action.async {
    Future.successful(Ok(views.html.index("Redirecting...")))
  }

  def auth = Action.async { implicit req =>
    Future {
      Ok {
        req.session.get("oauth-state") match {
          case Some(token) => s"""{"logged": true, "desc": "$token"}"""
          case None =>
            def callbackUrl = routes.Application.auth_(None, None).absoluteURL()
            lazy val state = UUID.randomUUID().toString

            OAuth2Solver.getAuthorizationUrl(callbackUrl)("repo")(state) match {
              case Some(url) => s"""{"logged": false, "desc": "$url"}""" //Redirect(url).withSession("oauth-state" -> state)
              case None => """{"logged": false, "desc": "AuthKey was not provided"}"""
            }
        }
      } as jsonApp
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

  def spreadsheet = Action.async { request =>
    Future {
      Ok("")
    }
  }
}