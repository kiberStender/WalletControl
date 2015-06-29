package br.com.wallet.types.loginTypes

/**
 * Created by sirkleber on 29/06/15.
 */
case class Google(clientId: Option[String], redirectUri: Option[String], secret: String) extends LoginTypes {

  def scope: String = "email%20profile"

  def authUrl: String = "https://accounts.google.com/o/oauth2/auth"
}
