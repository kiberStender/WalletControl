package br.com.wallet.persistence.dao

import org.joda.time.DateTime
import play.api.libs.Codecs

import scala.concurrent.Future
import br.com.wallet.persistence.dto.Balance
import anorm.SqlParser._

/**
 * Created by sirkleber on 13/08/15.
 */
object BalanceDao extends Dao {
  def getByTypeIdUserIdAndMonth(typeid: String, userid: String, actualMonth: Int) = queryRunnerManyS(
    "Select * from balance where acctypeid = {typeid} and accuserid = {userid} and extract(month from balancedate) = {month}"
  )(for {
    balanceid <- str("balanceid")
    calcbalance <- double("calcbalance")
    realbalance <- double("realbalance")
    balancedate <- date("balancedate")
  } yield Balance(balanceid, calcbalance, realbalance, balancedate))(
      'typeid -> typeid, 'month -> actualMonth, 'userid -> userid
  )

  def insertBalance: (Balance, String, String) => Future[Unit] = {
    case (Balance(balanceid, calcbalance, realbalance, balancedate), typeid, userid) => queryUpdate(
      """Insert into balance(balanceid, acctypeid, accuserid, calcbalance, realbalance, balancedate)
        | values({balanceid}, {typeid}, {accuserid}, {calcbalance}, {realbalance}, {balancedate})""".stripMargin
    )(
      'balanceid -> balanceid, 'typeid -> typeid, 'calcbalance -> calcbalance, 'realbalance -> realbalance,
      'balancedate -> balancedate, 'accuserid -> userid
    )
  }

  def updateBalance: (Balance, Boolean) => Future[Unit] = {
    case (Balance(balanceid, calcbalance, _, _), true) => queryUpdate(
      """Update balance set calcbalance = {calcbalance} where balanceid = {balanceid}"""
    )('calcbalance -> calcbalance, 'balanceid -> balanceid)
    case (Balance(balanceid, _, realbalance, _), false) => queryUpdate(
      """Update balance set realbalance = {realbalance} where balanceid = {balanceid}"""
    )('realbalance -> realbalance, 'balanceid -> balanceid)
  }

  def updateOrInsert: (Double, String, String, String) => Future[Unit] = {
    case (realbalance, usermail, typeid, userid) =>
      lazy val date = DateTime.now().plusMonths(1)
      def balances = getByTypeIdUserIdAndMonth(typeid, userid, date.getMonthOfYear)

      (date.toDate, balances) match {
        case (dt, List(Balance(id, _, balance, _))) => updateBalance(Balance(id, 0.0, balance + realbalance, dt), false)
        case (dt, _) =>
          def balanceid = Codecs.sha1(s"$usermail-${dt}")
          insertBalance(Balance(balanceid, 0.0, realbalance, dt), typeid, userid)
      }
  }
}
