package br.com.wallet.types.loginTypes

/**
 * Created by sirkleber on 29/06/15.
 */
trait LoginTypes {
  def clientId: Option[String]
  def secret: Option[String]
  def scope: String
  def authUrl: Option[String]
  def redirectUri: String

  def url: Option[String] = for {
    clId <- clientId
  } yield s"$authUrl?client_id=$clId&redirect_uri=$redirectUri&scope=$scope"
}
