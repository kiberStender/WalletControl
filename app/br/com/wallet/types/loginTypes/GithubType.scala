package br.com.wallet.types.loginTypes

import br.com.wallet.types.logonType.LogonType
import play.api.Configuration
import play.api.libs.json.JsValue

/**
 * Created by sirkleber on 13/07/15.
 */
class GithubType(
  _clientId: Option[String], _secret: Option[String]) extends LoginType{
  override def getQString(authId: String, authSec: String, code: String, redirectUri: => String): String = {
    s"client_id=$authId&client_secret=$authSec&code=$code"
  }

  override def clientId: Option[String] = _clientId

  override def secret: Option[String] = _secret

  override def tokenUrl: String = "https://github.com/login/oauth/access_token"

  override def userUrl: String = "https://api.github.com/user"

  override def scope: String = "user"

  override def authUrl: String = "https://github.com/login/oauth/authorize"

  override def provider: String =  "github"

  override def mapToLogonType: (JsValue) => LogonType = json => {
    //println(json)
    def username: String = (json \ "login").as[String]
    def usermail: String = (json \ "email").as[String]
    def profilePicture: String = (json \ "avatar_url").as[String]

    LogonType(username, usermail, profilePicture)
  }
}

object GithubType {
  def apply(conf: Configuration) = new GithubType(
    conf.getString("github.client.id"), conf.getString("github.client.secret")
  )
}