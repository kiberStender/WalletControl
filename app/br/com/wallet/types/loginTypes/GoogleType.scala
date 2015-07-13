package br.com.wallet.types.loginTypes

import play.api.Configuration

/**
 * Created by sirkleber on 13/07/15.
 */
object GoogleType extends LoginType(
  None, None, "google", "email%20profile&response_type=code",
  "https://accounts.google.com/o/oauth2/auth", "https://accounts.google.com/o/oauth2/token"
){
  def apply(conf: Configuration): LoginType = {
    lazy val clientId = conf getString s"$provider.client.id"
    lazy val secret = conf getString s"$provider.client.secret"
    LoginType(clientId, secret, provider, scope, authUrl, tokenUrl)
  }

  override def getQString(authId: String, authSec: String, code: String): List[(String, String)] =
    List("client_id" -> authId, "client_secret" -> authSec, "code" -> code)
}
