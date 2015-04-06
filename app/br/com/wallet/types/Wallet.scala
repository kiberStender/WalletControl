package br.com.wallet.types

import com.github.nscala_time.time.Imports._
import play.api.libs.Codecs._

/**
 * Created by sirkleber on 4/5/15.
 */
case class Wallet(
  acctypeid: String, description: String, balance: List[Balance], closingday: DateTime
) extends AccountType {
  def accname: String = "Wallet"
}

object Wallet {
  def apply (numeration: String, balance: List[Balance], clsingday: DateTime): Wallet = {
    def description = s"Cartão de crédito: $numeration"

    def acctypeid = clsingday.toString split 'T' match {
      case Array(date, _) => sha1(s"$date|$description|${DateTime.now}")
    }

    new Wallet(acctypeid, description, balance, clsingday)
  }
}
