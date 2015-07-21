package br.com.wallet.types.loginTypes

import br.com.wallet.types.loginOption.LoginOption
import br.com.wallet.types.logonType.LogonType
import play.api.{Application}
import play.api.http.{MimeTypes, HeaderNames}
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.mvc.{Call, RequestHeader}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by sirkleber on 29/06/15.
 */
abstract class LoginType {

  def clientId: Option[String]
  def secret: Option[String]
  def provider: String
  def scope: String
  def authUrl: String
  def tokenUrl: String
  def userUrl: String

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

  def getQString(authId: String, authSec: String, code: String, redirectUri: String): String

  def mapToLogonType: JsValue => LogonType

  def getToken(code: String, redirectUri: String)(implicit current: Application): Future[Option[LogonType]] = (for {
    authSec <- secret
    authId <- clientId
  } yield {
      def tokenResponse = WS.url(tokenUrl).
        withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.FORM, HeaderNames.ACCEPT -> MimeTypes.JSON).
        post(getQString(authId, authSec, code, redirectUri))

      for {
        response <- tokenResponse
      } yield Some(mapToLogonType(response.json))
  }).getOrElse(Future(None))
}

object LoginType {
  def apply(clientId: Option[String], secret: Option[String], provider: String): LoginType = provider match {
    case "google" => new GoogleType(clientId, secret)
    case "github" => new GithubType(clientId, secret)
  }
  def unapply(lt: LoginType): Option[(Option[String], Option[String], String)] = {
    Some(
      (lt.clientId, lt.secret, lt.provider)
    )
  }
  implicit val loginTypeFmt = Json.format[LoginType]
}