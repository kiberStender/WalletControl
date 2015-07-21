package br.com.wallet.types.token

import play.api.libs.json.{Format, Json}

/**
 * Created by sirkleber on 21/07/15.
 */
case class OauthToken(accessToken: String, tokenType: String, expiresIn: Int, idToken: String, refreshToken: String)

object OauthToken {
  implicit def tokenFormatter: Format[OauthToken] = Json.format[OauthToken]
}
