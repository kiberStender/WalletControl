package br.com.wallet.types.loginTypes

import br.com.wallet.types.loginOption.LoginOption
import play.api.{Application}
import play.api.http.{MimeTypes, HeaderNames}
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.mvc.{Results, Call, RequestHeader}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by sirkleber on 29/06/15.
 */
abstract class LoginType(
  _clientId: Option[String], _secret: Option[String], _provider: String, _scope: String, _authUrl: String, _tokenUrl: String
) {

  def clientId: Option[String] = _clientId
  def secret: Option[String] = _secret
  def provider: String = _provider
  def scope: String = _scope
  def authUrl: String = _authUrl
  def tokenUrl: String = _tokenUrl

  def authData(
    tp: (String, (String, Option[String], Option[String]) => Call)
  )(implicit req: RequestHeader): Option[LoginOption] = tp match {
    case (state, redirectUri) => for {
      clId <- clientId
    } yield {
        def rUri = redirectUri(provider, None, None).absoluteURL()
        LoginOption(provider, s"$authUrl?client_id=$clId&redirect_uri=$rUri&scope=$scope&state=$state")
      }
  }

  def getQString(authId: String, authSec: String, code: String): List[(String, String)]

  def getToken(code: String)(implicit current: Application): Future[Option[JsValue]] = (for {
    authSec <- secret
    authId <- clientId
  } yield {
      println(getQString(authId, authSec, code))
      def tokenResponse = WS.url(tokenUrl).
        withQueryString(getQString(authId, authSec, code): _*).
        withHeaders(HeaderNames.ACCEPT -> MimeTypes.JSON).
        post(Results.EmptyContent())

      for {
        response <- tokenResponse
      } yield Some(response.json)
  }).getOrElse(Future(None))
}

object LoginType {
  def apply(
    clientId: Option[String], secret: Option[String], provider: String, scope: String, authUrl: String, tokenUrl: String
  ): LoginType = provider match {
    case "google" => new GoogleType(clientId, secret, provider, scope, authUrl, tokenUrl)
    case "github" => new GithubType(clientId, secret, provider, scope, authUrl, tokenUrl)
  }
  def unapply(lt: LoginType): Option[(Option[String], Option[String], String, String, String, String)] = {
    Some((lt.clientId, lt.secret, lt.provider, lt.scope, lt.authUrl, lt.tokenUrl))
  }
  implicit val loginTypeWrite = Json.writes[LoginType]
  implicit val loginTypeFmt = Json.format[LoginType]
}