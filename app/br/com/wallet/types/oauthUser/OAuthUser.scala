package br.com.wallet.types.oauthUser

import br.com.wallet.types.loginTypes.LoginType
import br.com.wallet.types.logonType.LogonData$
import br.com.wallet.types.token.OauthToken
import play.api.libs.json.{JsValue, Json}

/**
 * Created by sirkleber on 12/07/15.
 */
case class OAuthUser (state: String, code: String, logonType: LogonData, token: OauthToken, actualLogin: LoginType)

object OAuthUser {
  implicit val formatter = Json.format[OAuthUser]
}
