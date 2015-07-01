package br.com.wallet.types.loginTypes

import play.api.Configuration

/**
 * Created by sirkleber on 29/06/15.
 */
case class Google(conf: Configuration, redirectUri: String, state: String) extends LoginTypes {

  def scope: String = "email%20profile"

  def authUrl: String = "https://accounts.google.com/o/oauth2/auth"

  def loginType: String = "google"
}
