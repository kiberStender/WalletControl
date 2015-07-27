package br.com.wallet.types.loginTypes

import br.com.wallet.types.loginOption.LoginOption
import br.com.wallet.types.logonType.LogonData
import br.com.wallet.types.token.OauthToken
import play.api.{Application}
import play.api.http.{MimeTypes, HeaderNames}
import play.api.libs.ws.WS
import play.api.mvc.{Call, RequestHeader}
import play.api.libs.json._
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

  def getQString(authId: String, authSec: String, code: String, redirectUri: => String): String

  def mapToLogonData: JsValue => LogonData

  def getToken(code: String, redirectUri: => String)(implicit current: Application): Future[Option[(OauthToken, LogonData)]] = (for {
    authSec <- secret
    authId <- clientId
  } yield {
      def tokenResponse: Future[OauthToken] = WS.url(tokenUrl).
        withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.FORM, HeaderNames.ACCEPT -> MimeTypes.JSON).
        post(getQString(authId, authSec, code, redirectUri)) map { wsResponse =>
          lazy val json = wsResponse.json

          OauthToken(
            (json \ "access_token").as[String], (json \ "token_type").as[String], (json \ "expires_in").asOpt[Int]
          )
      }

      def userInfo: String => Future[LogonData] = accessToken => WS.url(s"$userUrl?access_token=$accessToken").
        withHeaders(HeaderNames.ACCEPT -> MimeTypes.JSON).
        get() map { wsResponse => mapToLogonData(wsResponse.json) }

      for {
        token <- tokenResponse
        user <- userInfo(token.accessToken)
      } yield Some((token, user))
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