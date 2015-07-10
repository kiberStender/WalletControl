package br.com.wallet.types.loginTypes

import play.api.Configuration

/**
 * Created by sirkleber on 01/07/15.
 */
case class GitHubType(clientId: Option[String], secret: Option[String]) extends LoginType {
  def loginType: String = "github"

  def scope: String = "repo"

  def authUrl: String = "https://github.com/login/oauth/authorize"

  def tokenUri: String = "https://github.com/login/oauth/access_token"
}

object GitHubType {
  def apply(conf: Configuration) = {
    lazy val provider = "github"
    GitHubType((conf.getString(s"$provider.")), (conf.getString(s"$provider.")))
  }
}