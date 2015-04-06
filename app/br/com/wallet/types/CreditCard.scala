package br.com.wallet.types

import com.github.nscala_time.time.Imports._
import play.api.libs.Codecs._

/**
 * Created by sirkleber on 4/5/15.
 */
case class CreditCard(
  acctypeid: String, description: String, balance: List, closingday: DateTime
) extends AccountType {
  def accname: String = "CreditCard"
}

object CreditCard {
  def apply (numeration: String, bal: List, clsingday: DateTime): CreditCard = {
    def description = s"Cartão de crédito: $numeration"

    def acctypeid = clsingday.toString split 'T' match {
      case Array(date, _) => sha1(s"$date|$description")
    }

    new CreditCard(acctypeid, description, bal, clsingday)
  }
}
