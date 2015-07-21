package br.com.wallet.types.logonType

import play.api.libs.json.{Json, Format}

/**
 * Created by sirkleber on 20/07/15.
 */
case class LogonType(username: String, usermail: String, profilePicture: String)

object LogonType {
  implicit def LogonTypeFormatter: Format[LogonType] = Json.format[LogonType]
}
