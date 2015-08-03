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

  protected def oauthUserSession = "oauth_user"
  protected def oauthStateSesion = "oauth_state"
}