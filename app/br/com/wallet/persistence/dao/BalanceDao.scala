package br.com.wallet.persistence.dao

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
}
