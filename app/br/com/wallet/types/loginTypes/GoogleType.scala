package br.com.wallet.types.loginTypes

import play.api.Configuration

/**
 * Created by sirkleber on 29/06/15.
 */
case class GoogleType(clientId: Option[String], secret: Option[String]) extends LoginType {
  def loginType: String = "google"

  def scope: String = "email%20profile&response_type=code"

  def authUrl: String = "https://accounts.google.com/o/oauth2/auth"

  def tokenUri: String = "https://accounts.google.com/o/oauth2/token"
}

object GoogleType {
  def apply(conf: Configuration) = {
    def provider = "google"
    GoogleType((conf.getString(s"$provider.")), (conf.getString(s"$provider.")))
  }
}