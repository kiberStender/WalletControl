package br.com.wallet.persistence.dao

import scala.concurrent.Future
import br.com.wallet.persistence.dto.Balance
import anorm.SqlParser._

/**
 * Created by sirkleber on 13/08/15.
 */
object BalanceDao extends Dao {
  def getLastBalanceByTypeId(typeid: String) = queryRunnerManyS(
    "Select * from balance where typeid = {typeid} order by balancedate asc"
  )(for {
    balanceid <- str("balanceid")
    calcbalance <- double("calcbalance")
    realbalance <- double("realbalance")
    balancedate <- date("balancedate")
  } yield Balance(balanceid, calcbalance, realbalance, balancedate))('typeid -> typeid)

  def insertBalance: (Balance, String) => Future[Unit] = {
    case (Balance(balanceid, calcbalance, realbalance, balancedate), typeid) => queryUpdate(
      "Insert into balance(balanceid, typeid, calcbalance, realbalance, balancedate) values({balanceid}, {typeid}, {calcbalance}, {realbalance}, {balancedate})"
    )('balanceid -> balanceid, 'typeid -> typeid, 'calcbalance -> calcbalance, 'realbalance -> realbalance, 'balancedate -> balancedate)
  }
}
