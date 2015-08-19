package br.com.wallet.persistence.dto

import play.api.libs.json.{Format, Json}

/**
 * Created by sirkleber on 13/08/15.
 */
case class AccountType(
  accountTypeId: String, accName: String, description: String, closingDay: String, items: Seq[Item], balances: Seq[Balance]
) {
  override def toString = {
    s"{accountTypeId: $accountTypeId, description: $description, accName: $accName, closingDay: $closingDay, items: $items, balance: $balances}"
  }
}

object AccountType {
  implicit def formats: Format[AccountType] = Json.format[AccountType]
}
