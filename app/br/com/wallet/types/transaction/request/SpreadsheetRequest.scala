package br.com.wallet.types.transaction.request

import play.api.libs.json.{Json, Format}

/**
 * Created by sirkleber on 12/08/15.
 */
case class SpreadsheetRequest(method: String) extends AbstractRequest

object SpreadsheetRequest {
  implicit def formatter: Format[SpreadsheetRequest] = Json.format[SpreadsheetRequest]
}
