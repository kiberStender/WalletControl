package br.com.wallet.types.loginTypes

import play.api.Configuration

/**
 * Created by sirkleber on 13/07/15.
 */
class GithubType(
  clientId: Option[String], secret: Option[String], provider: String, scope: String, authUrl: String, tokenUrl: String
) extends LoginType(clientId, secret, provider, scope, authUrl, tokenUrl){
  override def getQString(authId: String, authSec: String, code: String, redirectUri: String): String = {
    s"client_id=$authId&client_secret=$authSec&code=$code"
  }
}

object GithubType {
  def apply(conf: Configuration) = new GithubType(
    conf.getString("github.client.id"), conf.getString("github.client.secret"), "github",
    "user", "https://github.com/login/oauth/authorize","https://github.com/login/oauth/access_token"
  )
}