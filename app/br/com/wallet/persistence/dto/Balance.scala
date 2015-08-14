package br.com.wallet.persistence.dto

import com.github.nscala_time.time.Imports._
import play.api.libs.json.{Json, Format}

/**
 * Created by sirkleber on 13/08/15.
 */
case class Balance(balanceid: String, typeid : String, calcbalance: Double, realbalance: Double, balancedate: DateTime) extends Dto

object Balance{
  implicit def formatter: Format[Balance] = Json.format[Balance]
}
