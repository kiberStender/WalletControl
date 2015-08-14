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

object CustomAccount {
  implicit def formats = Json.format[CustomAccount]
}