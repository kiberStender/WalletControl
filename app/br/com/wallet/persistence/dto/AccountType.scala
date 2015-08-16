package br.com.wallet.persistence.dto

import play.api.libs.json.Json

/**
 * Created by sirkleber on 13/08/15.
 */
sealed abstract class AccountType {
  def accountTypeId: String
  def description: String
  def accName: String
  def closingDay: String
  def items: Seq[Item]
  def balances: Seq[Balance]

  def toCustom: CustomAccount = CustomAccount(accountTypeId, accName, description, closingDay, items, balances)

  override def toString = {
    s"{accountTypeId: $accountTypeId, description: $description, accName: $accName, closingDay: $closingDay, items: $items, balance: $balances}"
  }
}

final case class Wallet(
  accountTypeId: String, closingDay: String, items: Seq[Item], balances: Seq[Balance]
) extends AccountType{
  override def accName: String = "Wallet"

  override def description: String = "Carteira"
}

final case class CreditCard(
  accountTypeId: String, closingDay: String, items: Seq[Item], balances: Seq[Balance]
) extends AccountType{
  override def accName: String = "CreditCard"

  override def description: String = "Cartão de crédito"
}

final case class CustomAccount(
  accountTypeId: String, accName: String, description: String, closingDay: String, items: Seq[Item], balances: Seq[Balance]
) extends AccountType

object AccountType {
  def apply(id: String, accName: String, desc: String, closingDay: String, items: Seq[Item], bal: Seq[Balance]) = accName match {
    case "Wallet" => Wallet(id, closingDay, items, bal)
    case "CreditCard" => CreditCard(id, closingDay, items, bal)
    case _ => CustomAccount(id, accName, desc, closingDay, items, bal)
  }

  def unapply(at: AccountType): Option[(String, String, String, String, Seq[Item], Seq[Balance])] = {
    Some((at.accountTypeId, at.accName, at.description, at.closingDay, at.items, at.balances))
  }
}

object CustomAccount {
  implicit def formats = Json.format[CustomAccount]
}
