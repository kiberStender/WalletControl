package br.com.wallet.api.controller

import akka.util.Timeout
import scala.concurrent.duration._
import play.api.mvc._

/**
 * Created by sirkleber on 4/7/15.
 */
trait ActionController extends Controller {
  protected implicit lazy val timeout = Timeout(3 second)

  protected def jsonApp = "application/json"

  protected def oauthUserSession = "oauth_user"
  protected def oauthStateSesion = "oauth_state"
}