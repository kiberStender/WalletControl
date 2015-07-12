package br.com.wallet.types.oauthUser

import br.com.wallet.types.loginTypes.LoginType
import play.api.libs.json.Json

/**
 * Created by sirkleber on 12/07/15.
 */
case class OAuthUser (user: String, state: String, code: String, actualLogin: LoginType)

object OAuthUser {
  implicit val writesUser = Json.writes[OAuthUser]
  implicit val formatter = Json.format[OAuthUser]
}
