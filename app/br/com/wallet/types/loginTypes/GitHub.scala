package br.com.wallet.types.loginTypes

import play.api.Configuration

/**
 * Created by sirkleber on 01/07/15.
 */
case class GitHub(conf: Configuration, redirectUri: String) extends LoginTypes{
  override def loginType: String = "github"

  override def scope: String = "repo"

  override def authUrl: String = "https://github.com/login/oauth/authorize"
}
