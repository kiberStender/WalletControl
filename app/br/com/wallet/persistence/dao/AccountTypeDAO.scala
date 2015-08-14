package br.com.wallet.persistence.dao

import br.com.wallet.persistence.dto.{CustomAccount, CreditCard, Wallet}

import anorm.SqlParser._

/**
 * Created by sirkleber on 13/08/15.
 */
object AccountTypeDAO extends Dao {
  def getByUserId(userid: String) = queryRunnerSingle("Select * from accounttype where userid = {userid}")(
  for {
    accountTypeId <- str("")
    accName <- str("")
    description <- str("")
    closingDay <- str("")
    items <- ItemDAO.getItemsByAccountType(accountTypeId)
  } yield accName match {
    case "Wallet" => Wallet(accountTypeId, closingDay, items, Nil)
    case "CreditCard" => CreditCard(accountTypeId, closingDay, items, Nil)
    case _ => CustomAccount(accountTypeId, accName, description, closingDay, items, Nil)
  })("userid" -> userid)

}
