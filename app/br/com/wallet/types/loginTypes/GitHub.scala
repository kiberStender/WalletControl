package br.com.wallet.types.loginTypes

import play.api.Configuration
import play.api.mvc.Call

/**
 * Created by sirkleber on 01/07/15.
 */
case class GitHub(
  conf: Configuration, redirectUri:(String, Option[String], Option[String]) => Call
) extends LoginType {
  override def loginType: String = "github"

  override def scope: String = "repo"

  override def authUrl: String = "https://github.com/login/oauth/authorize"

  def tokenUri: String = "https://github.com/login/oauth/access_token"
}
