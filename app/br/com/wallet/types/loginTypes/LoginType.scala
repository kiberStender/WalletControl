package br.com.wallet.types.loginTypes

import br.com.wallet.types.loginOption.LoginOption
import play.api.{Application, Configuration}
import play.api.http.{MimeTypes, HeaderNames}
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.mvc.{Results, Call, RequestHeader}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by sirkleber on 29/06/15.
 */
sealed case class LoginType(
  clientId: Option[String], secret: Option[String], provider: String, scope: String, authUrl: String, tokenUrl: String
) {
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

  def getToken(code: String)(implicit current: Application): Future[Option[JsValue]] = (for {
    authSec <- secret
    authId <- clientId
  } yield {
      def tokenResponse = WS.url(tokenUrl).
        withQueryString("client_id" -> authId, "client_secret" -> authSec, "code" -> code).
        withHeaders(HeaderNames.ACCEPT -> MimeTypes.JSON).
        post(Results.EmptyContent())

      for {
        response <- tokenResponse
      } yield Some(response.json)
  }).getOrElse(Future(None))
}

object LoginType {
  implicit val loginTypeWrite = Json.writes[LoginType]
  implicit val loginTypeFmt = Json.format[LoginType]
}

object GithubType extends LoginType(
  None, None, "github", "repo",
  "https://github.com/login/oauth/authorize", "https://github.com/login/oauth/access_token"
){
  def apply(conf: Configuration): LoginType = {
    lazy val clientId = conf getString s"$provider.client.id"
    lazy val secret = conf getString s"$provider.client.secret"
    LoginType(clientId, secret, provider, scope, authUrl, tokenUrl)
  }
}

object GoogleType extends LoginType(
  None, None, "google", "email%20profile&response_type=code",
  "https://accounts.google.com/o/oauth2/auth", "https://accounts.google.com/o/oauth2/token"
){
  def apply(conf: Configuration): LoginType = {
    lazy val clientId = conf getString s"$provider.client.id"
    lazy val secret = conf getString s"$provider.client.secret"
    LoginType(clientId, secret, provider, scope, authUrl, tokenUrl)
  }
}