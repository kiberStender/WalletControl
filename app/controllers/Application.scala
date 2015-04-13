package controllers

import br.com.wallet.api.actors.AuthActor
import br.com.wallet.api.controller.ActionController
import br.com.wallet.api.models.result.{Success, Failure}
import play.api.libs.oauth.{RequestToken, ServiceInfo, OAuth, ConsumerKey}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor._
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import akka.pattern.ask

object Application extends ActionController {
  private implicit val timeout = Timeout(3 second)
  //private def jsonApp = "application/json"
  //private def hash = "usuario"
  private lazy val auth = Akka.system.actorOf(Props[AuthActor])

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def login = Action.async {
    Future(Ok("Login"))
  }

  def getAllItens = Action.async { implicit req =>
    sessionTokenPair match {
      case Some(_) => Future(Ok("Kiber"))
      case None => Future.successful(Redirect(routes.Application.login))
    }
  }
}