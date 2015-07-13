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
case class LoginType(
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

  def getQString(authId: String, authSec: String, code: String): List[(String, String)] = List()

  def getToken(code: String)(implicit current: Application): Future[Option[JsValue]] = (for {
    authSec <- secret
    authId <- clientId
  } yield {
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
  implicit val loginTypeWrite = Json.writes[LoginType]
  implicit val loginTypeFmt = Json.format[LoginType]
}