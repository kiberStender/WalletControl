package br.com.wallet.persistence.dao

import scala.concurrent.Future
import br.com.wallet.persistence.dto.Balance
import anorm.SqlParser._

/**
 * Created by sirkleber on 13/08/15.
 */
object BalanceDao extends Dao {
  def getBalanceByTypeIdAndMonth(typeid: String, actualMonth: Int) = queryRunnerManyS(
    "Select * from balance where typeid = {typeid} and extract(month from balancedate) = {month}"
  )(for {
    balanceid <- str("balanceid")
    calcbalance <- double("calcbalance")
    realbalance <- double("realbalance")
    balancedate <- date("balancedate")
  } yield Balance(balanceid, calcbalance, realbalance, balancedate))('typeid -> typeid, 'month -> actualMonth)

  def insertBalance: (Balance, String) => Future[Unit] = {
    case (Balance(balanceid, calcbalance, realbalance, balancedate), typeid) => queryUpdate(
      "Insert into balance(balanceid, typeid, calcbalance, realbalance, balancedate) values({balanceid}, {typeid}, {calcbalance}, {realbalance}, {balancedate})"
    )('balanceid -> balanceid, 'typeid -> typeid, 'calcbalance -> calcbalance, 'realbalance -> realbalance, 'balancedate -> balancedate)
  }
}
