package br.com.wallet.types.loginOption

import play.api.libs.json.Format
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Created by sirkleber on 03/07/15.
 */
sealed case class LoginOption (provider: String, authUri: String)

object LoginOption {
  implicit def formatter: Format[LoginOption] = (
    (__ \ "provider").format[String] ~ (__ \ "authUri").format[String]
    )(LoginOption.apply, unlift(LoginOption.unapply))
}
