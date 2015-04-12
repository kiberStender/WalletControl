package br.com.wallet.api.controller

import br.com.wallet.api.models.result.Failure
import br.com.wallet.types.AccUser
import play.api.mvc.Controller
import play.api.mvc.Action
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.JsObject
import play.api.mvc.Request
import play.api.libs.json.JsValue
import play.api.libs.json.Json

/**
 * Created by sirkleber on 4/7/15.
 */
trait ActionController extends Controller {

  protected def jsonApp = "application/json"

  protected def hash = "usuario"

  protected def getAction(f: AccUser => JsObject) = Action.async { request =>
    Future {
      Ok(request.session.get(hash) match {
        case None => Failure("Não logado") toJson
        case Some(us) => f((Json parse us).as[AccUser])
      }) as jsonApp
    }
  }

  protected def postAction(f: (AccUser, Request[JsValue]) => JsObject) = Action.async(parse.json) { request =>
    Future {
      Ok(request.session.get(hash) match {
        case None => Failure("Não logado") toJson
        case Some(us) => f((Json parse us).as[AccUser], request)
      }) as jsonApp
    }
  }
}