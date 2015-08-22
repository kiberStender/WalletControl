package br.com.wallet.persistence.dto

import java.util.Date

import play.api.libs.json.{Json, Format}

/**
 * Created by sirkleber on 13/08/15.
 */
case class Balance(balanceid: String, calcbalance: Double, realbalance: Double, balancedate: Date) extends Dto

object Balance{
  implicit def formatter: Format[Balance] = Json.format[Balance]
}
