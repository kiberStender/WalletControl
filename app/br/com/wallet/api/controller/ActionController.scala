package br.com.wallet.api.controller

import akka.util.Timeout
import br.com.wallet.api.models.result.{Success, Failure}
import br.com.wallet.types.AccUser
import scala.concurrent.duration._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{JsValue, JsObject, Json}

/**
 * Created by sirkleber on 4/7/15.
 */
trait ActionController extends Controller {
  protected implicit lazy val timeout = Timeout(3 second)
  protected object NaoLogado extends Failure("Não logado!!!")

  protected def jsonApp = "application/json"

  protected def hash = "usuario"

  protected type ActP[A] = (A, Request[JsValue]) => Future[Result]

  protected def post(f: (Option[String], Request[JsValue]) => Future[Result]): Action[JsValue] = Action.async(parse.json) {
    req => f(req.session.get(hash), req)
  }

  protected def getAction(f: AccUser => JsObject) = Action.async { request =>
    Future {
      Ok(request.session.get(hash) match {
        case None => Failure("Não logado") toJson
        case Some(us) => f((Json parse us).as[AccUser])
      }) as jsonApp
    }
  }
}