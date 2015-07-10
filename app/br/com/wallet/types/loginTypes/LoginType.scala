package br.com.wallet.types.loginTypes

import br.com.wallet.types.loginOption.LoginOption
import play.api.mvc.{Call, RequestHeader}

/**
 * Created by sirkleber on 29/06/15.
 */
trait LoginType {
  protected def loginType: String
  protected def clientId: Option[String]
  protected def secret: Option[String]
  protected def scope: String
  protected def authUrl: String

  def tokenUri: String

  def authData(
    tp: (String, (String, Option[String], Option[String]) => Call)
  )(implicit req: RequestHeader): Option[LoginOption] = tp match {
    case (state, redirectUri) => for {
      clId <- clientId
    } yield {
        def rUri = redirectUri(loginType, None, None).absoluteURL()
        LoginOption(loginType, s"$authUrl?client_id=$clId&redirect_uri=$rUri&scope=$scope&state=$state")
      }
  }
}
