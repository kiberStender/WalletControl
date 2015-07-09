package br.com.wallet.types.loginTypes

import play.api.Configuration
import play.api.mvc.Call

/**
 * Created by sirkleber on 29/06/15.
 */
case class Google(
  conf: Configuration, redirectUri:(String, Option[String], Option[String]) => Call
) extends LoginType {
  def loginType: String = "google"

  def scope: String = "email%20profile&response_type=code"

  def authUrl: String = "https://accounts.google.com/o/oauth2/auth"

  def tokenUri: String = "https://accounts.google.com/o/oauth2/token"
}
