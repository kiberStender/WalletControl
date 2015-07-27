package br.com.wallet.types.logonType

import play.api.libs.json.{Json, Format}

/**
 * Created by sirkleber on 20/07/15.
 */
case class LogonData(username: String, usermail: String, profilePicture: String)

object LogonData {
  implicit def LogonTypeFormatter: Format[LogonData] = Json.format[LogonData]
}
