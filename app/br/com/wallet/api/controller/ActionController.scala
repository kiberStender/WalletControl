package br.com.wallet.api.controller

import br.com.wallet.api.models.result.{Success, Failure}
import br.com.wallet.types.AccUser
import controllers.Application._
import controllers.routes
import play.api.libs.oauth.{RequestToken, ServiceInfo, OAuth, ConsumerKey}
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{JsValue, JsObject, Json}

/**
 * Created by sirkleber on 4/7/15.
 */
trait ActionController extends Controller {
  protected object NaoLogado extends Failure("Não logado!!!")

  protected def jsonApp = "application/json"

  protected def hash = "usuario"

  protected type ActP[A] = (A, Request[JsValue]) => Future[Result]

  def key = ConsumerKey("", "")
  def google = OAuth(ServiceInfo(
    "https://accounts.google.com/o/oauth2/token",
    "https://your.domain.tld/authenticate/google",
    "https://accounts.google.com/o/oauth2/auth", key), false)

  protected def authenticate = Action { req =>
    req.getQueryString("oauth_verifier").map { verifier =>
      val tokenPair = sessionTokenPair(req).get

      google.retrieveAccessToken(tokenPair, verifier) match {
        case Right(t) => Redirect(routes.Application.index).withSession("token" -> t.token, "secret" -> t.secret)
        case Left(e) => throw e
      }
    }.getOrElse(google.retrieveRequestToken("http://localhost:9000/auth") match {
      case Right(t) => Redirect(google.redirectUrl(t.token)).withSession("token" -> t.token, "secret" -> t.secret)
      case Left(e) => throw e
    })
  }

  protected def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = for {
    token <- request.session.get("token")
    secret <- request.session.get("secret")
  } yield RequestToken(token, secret)

  protected def notLoggedIn = Future(Ok(NaoLogado.toJson) as jsonApp)

  protected def post(f: (Option[String], Request[JsValue]) => Future[Result]): Action[JsValue] = Action.async(parse.json) {
    req => f(req.session.get(hash), req)
  }

  protected def postAction(f: (AccUser, JsValue) => Future[Result]): Action[JsValue] =  post { (us, req) => us match {
    case None => notLoggedIn
    case Some(user) => f((Json parse user).as[AccUser], req.body)
  }
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