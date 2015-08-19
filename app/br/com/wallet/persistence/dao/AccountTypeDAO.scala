package br.com.wallet.persistence.dao

import br.com.wallet.persistence.dto.AccountType

import anorm.SqlParser._

import scala.concurrent.Future

/**
 * Created by sirkleber on 13/08/15.
 */
object AccountTypeDAO extends Dao {

  def insert: AccountType => String => Future[Unit] = {
    case AccountType(id, accName, desc, closingDay, _, _) => userid => queryUpdate(
      "Insert into accounttype(acctypeid, accuserid, description, closingday, accname) values({id}, {userid}, {desc}, {closingday}, {accname})"
    )("id" -> id, "userid" -> userid, "accname" -> accName, "desc" -> desc, "closingday" -> closingDay)
  }
  def getByUserId(userid: String): Future[List[AccountType]] = queryRunnerMany("Select * from accounttype where accuserid = {userid}")(for {
    accountTypeId <- str("acctypeid")
    accName <- str("accname")
    description <- str("description")
    closingDay <- str("closingday")
  } yield AccountType(accountTypeId, accName, description, closingDay, ItemDAO.getItemsByAccountType(accountTypeId), Nil)
  )("userid" -> userid)
}