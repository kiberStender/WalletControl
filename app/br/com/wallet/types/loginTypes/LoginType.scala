package br.com.wallet.types.loginTypes

import br.com.wallet.types.loginOption.LoginOption
import play.api.Configuration
import play.api.mvc.{Call, RequestHeader}

/**
 * Created by sirkleber on 29/06/15.
 */
trait LoginType {
  protected val conf: Configuration
  protected def loginType: String
  protected def clientId: Option[String] = conf getString s"${loginType}.client.id"
  protected def secret: Option[String] = conf getString s"${loginType}.client.secret"
  protected def scope: String
  protected def authUrl: String
  protected def redirectUri: (String, Option[String], Option[String]) => Call

  def tokenUri: String

  def authData(state: String)(implicit req: RequestHeader): Option[LoginOption] = for {
    clId <- clientId
  } yield {
      lazy val rUri = redirectUri(loginType, None, None).absoluteURL()
      LoginOption(loginType, s"$authUrl?client_id=$clId&redirect_uri=$rUri&scope=$scope&state=$state")
    }
}
