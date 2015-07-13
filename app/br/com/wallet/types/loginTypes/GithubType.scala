package br.com.wallet.types.loginTypes

import play.api.Configuration

/**
 * Created by sirkleber on 13/07/15.
 */
object GithubType extends LoginType(
  None, None, "github", "repo",
  "https://github.com/login/oauth/authorize", "https://github.com/login/oauth/access_token"
){
  def apply(conf: Configuration): LoginType = {
    lazy val clientId = conf getString s"$provider.client.id"
    lazy val secret = conf getString s"$provider.client.secret"
    LoginType(clientId, secret, provider, scope, authUrl, tokenUrl)
  }

  override def getQString(authId: String, authSec: String, code: String): List[(String, String)] =
    List("client_id" -> authId, "client_secret" -> authSec, "code" -> code)
}