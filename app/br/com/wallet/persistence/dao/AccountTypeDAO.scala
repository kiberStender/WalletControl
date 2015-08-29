package br.com.wallet.persistence.dao

import br.com.wallet.persistence.dto.AccountType

import anorm.SqlParser._
import org.joda.time.DateTime

import scala.concurrent.Future

import br.com.wallet.persistence.dao.BalanceDao._
import br.com.wallet.persistence.dao.ItemDAO._

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
  } yield {
      def actualMonth = DateTime.now().monthOfYear().get()
      AccountType(
        accountTypeId, accName, description, closingDay, getItemsByAccountType(accountTypeId), getBalanceByTypeIdAndMonth(accountTypeId, actualMonth)
      )
    }
  )("userid" -> userid)
}