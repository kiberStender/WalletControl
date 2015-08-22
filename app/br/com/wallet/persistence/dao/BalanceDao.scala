package br.com.wallet.persistence.dao

import br.com.wallet.persistence.dto.Balance
import anorm.SqlParser._

/**
 * Created by sirkleber on 13/08/15.
 */
object BalanceDao extends Dao {
  def getLastBalanceByTypeId(typeid: String) = queryRunnerManyS(
    "Select * from balance where typeid = {typeid} group by balancedate asc"
  )(for {
    balanceid <- str("balanceid")
    calcbalance <- double("")
    realbalance <- double("realbalance")
    balancedate <- date("balancedate")
  } yield Balance(balanceid, calcbalance, realbalance, balancedate))('typeid -> typeid)
}
