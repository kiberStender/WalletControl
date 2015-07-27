package br.com.wallet.types.loginTypes

import br.com.wallet.types.logonType.LogonData
import play.api.Configuration
import play.api.libs.json.JsValue

/**
 * Created by sirkleber on 13/07/15.
 */
class GoogleType(
  _clientId: Option[String], _secret: Option[String]) extends LoginType{
  override def getQString(authId: String, authSec: String, code: String, redirectUri: => String): String = {
    s"client_id=$authId&client_secret=$authSec&code=$code&grant_type=authorization_code&redirect_uri=$redirectUri"
  }

  override def clientId: Option[String] = _clientId

  override def secret: Option[String] = _secret

  override def tokenUrl: String = "https://accounts.google.com/o/oauth2/token"

  override def userUrl: String = "https://www.googleapis.com/oauth2/v1/userinfo"

  override def scope: String = "email%20profile&response_type=code"

  override def authUrl: String = "https://accounts.google.com/o/oauth2/auth"

  override def provider: String = "google"

  override def mapToLogonData: JsValue => LogonData =  json => {
    //println(json)
    def username: String = (json \ "name").as[String]
    def usermail: String = (json \ "email").as[String]
    def profilePicture: String = (json \ "picture").as[String]

    LogonData(username, usermail, profilePicture)
  }
}

object GoogleType {
  def apply(conf: Configuration) = new GoogleType(
    conf.getString("google.client.id"), conf.getString("google.client.secret")
  )
}