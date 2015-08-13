package br.com.wallet.persistence.dto

/**
 * Created by sirkleber on 13/08/15.
 */
sealed trait AccountType {
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
  override def accName: String = "Carteira"

  override def description: String = "Carteira simples que anda com o usuário contendo dinheiro físico"
}

final case class CreditCard(
  accountTypeId: String, closingDay: String, items: Seq[Item], balances: Seq[Balance]
) extends AccountType{
  override def accName: String = "Cartão de crédito"

  override def description: String = "Cartão magnético contendo dinheiro virtual proveniente de uma conta bancária"
}

final case class CustomAccount(
  accountTypeId: String, accName: String, description: String, closingDay: String, items: Seq[Item], balances: Seq[Balance]
) extends AccountType