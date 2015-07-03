package br.com.wallet.types.loginTypes

import br.com.wallet.types.loginOption.LoginOption
import play.api.Configuration

/**
 * Created by sirkleber on 29/06/15.
 */
trait LoginTypes {
  protected val conf: Configuration
  protected def loginType: String
  protected def clientId: Option[String] = conf getString s"${loginType}.client.id"
  protected def secret: Option[String] = conf getString s"${loginType}.client.secret"
  protected def scope: String
  protected def authUrl: String
  protected def redirectUri: String

  def authData: String => Option[LoginOption] = state => for {
    clId <- clientId
  } yield LoginOption(loginType, s"$authUrl?client_id=$clId&redirect_uri=$redirectUri&scope=$scope&state=$state")
}
