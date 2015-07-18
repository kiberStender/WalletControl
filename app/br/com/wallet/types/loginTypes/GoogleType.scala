package br.com.wallet.types.loginTypes

import play.api.Configuration
import play.api.libs.json.Json

/**
 * Created by sirkleber on 13/07/15.
 */
class GoogleType(
  _clientId: Option[String], _secret: Option[String], _provider: String, _scope: String, _authUrl: String, _tokenUrl: String
) extends LoginType(_clientId, _secret, _provider, _scope, _authUrl, _tokenUrl){
  override def getQString(authId: String, authSec: String, code: String, redirectUri: String): String = {
    s"client_id=$authId&client_secret=$authSec&code=$code&grant_type=authorization_code&redirect_uri=$redirectUri"
  }
}

object GoogleType {
  def apply(conf: Configuration) = new GoogleType(
    conf.getString("google.client.id"), conf.getString("google.client.secret"), "google",
    "email%20profile&response_type=code", "https://accounts.google.com/o/oauth2/auth", "https://accounts.google.com/o/oauth2/token"
  )
}