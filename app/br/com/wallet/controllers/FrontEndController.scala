package br.com.wallet.controllers

import br.com.wallet.api.controller.ActionController
import play.api.mvc.Action
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.language.postfixOps

import scala.concurrent.Future

/**
 * Created by sirkleber on 29/06/15.
 */
object FrontEndController extends ActionController {
  def index = Action.async {
    Future.successful(Ok(views.html.index("Redirecting...")))
  }

  def spreadsheet = Action.async { request =>
    Future {
      request.session.get("user") match {
        case Some(_) => Ok(views.html.spreadsheet(""))
        case None => Ok(views.html.index("Redirecting...")) withNewSession
      }
    }
  }

  def logoff = Action.async { implicit req =>
    Future.successful(Ok (views.html.index("Redirecting...")) withNewSession)
  }
}
