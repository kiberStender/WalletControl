package br.com.wallet.types

/**
 * Created by sirkleber on 29/06/15.
 */
trait LoginTypes {
  def clientId: String
  def secret: String
  def scope: String
  def authUrl: String
  def redirectUri: String

  def url = s"$authUrl?client_id=$clientId&redirect_uri=$redirectUri&scope=$scope"
}
