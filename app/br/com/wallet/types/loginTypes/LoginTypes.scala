package br.com.wallet.types.loginTypes

import play.api.Configuration

/**
 * Created by sirkleber on 29/06/15.
 */
trait LoginTypes {
  val conf: Configuration
  def loginType: String
  def clientId: Option[String] = conf.getString(s"${loginType}.client.id")
  def secret: Option[String] = conf.getString(s"${loginType}.client.secret")
  def scope: String
  def authUrl: String
  def redirectUri: String

  def url = for {
    clId <- clientId
  } yield s"$authUrl?client_id=$clId&redirect_uri=$redirectUri&scope=$scope"
}
