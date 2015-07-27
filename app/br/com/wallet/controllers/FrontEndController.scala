package br.com.wallet.controllers

import br.com.wallet.api.controller.ActionController
import br.com.wallet.api.models.result.{Failure, Success}
import br.com.wallet.types.oauthUser.OAuthUser
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.language.postfixOps

import scala.concurrent.Future

/**
 * Created by sirkleber on 29/06/15.
 */
class FrontEndController extends ActionController {
  def index = Action.async {
    Future.successful(Ok(views.html.index("Redirecting...")))
  }

  def spreadsheet = Action.async { request =>
    Future {
      request.session.get(oauthUserSession) match {
        case Some(body) => Ok(views.html.spreadsheet("Spreadsheet"))
        case None => Redirect("/") withNewSession
      }
    }
  }

  def getData = Action.async { request =>
    Future {
      Ok(request.session.get(oauthUserSession) match {
        case Some(body) => Success(Json.parse(body).as[OAuthUser]) toJson
        case None => Failure("Usuário não logado") toJson
      }) as jsonApp
    }

  }

  def logoff = Action.async { implicit req =>
    Future.successful(Redirect("/").withNewSession)
  }
}
