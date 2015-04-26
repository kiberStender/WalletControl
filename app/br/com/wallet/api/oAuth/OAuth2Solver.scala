package br.com.wallet.api.oAuth

import play.api.Application
import play.api.Play

/**
 * Created by sirkleber on 26/04/15.
 */
object OAuth2Solver {
  lazy val app: Application = Play.current
  private def authKey: Option[String] = app.configuration.getString("github.client.id")
  private def authSec: Option[String] = app.configuration.getString("github.client.secret")

  def getAuthorizationUrl: String => String => String => Option[String] = redirectUri => scope => state => for {
    baseUrl <- app.configuration.getString("github.redirect.url")
    authId <- app.configuration.getString("github.client.id")
  } yield baseUrl.format(authId, scope, state)
}
