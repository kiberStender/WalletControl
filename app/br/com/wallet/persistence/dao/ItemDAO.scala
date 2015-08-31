package br.com.wallet.persistence.dao

import anorm.SqlParser._
import br.com.wallet.persistence.dto.Item

/**
 * Created by sirkleber on 13/08/15.
 */
object ItemDAO extends Dao {

  def getMonthItemsByAccountType(acctypeId: String, userid: String, month: Int) = queryRunnerManyS(
    """Select i.itemid, t.transactiontypename, i.description, i.itemvalue, i.purchasedate
      From item i join transactiontype t on t.transactiontypeid = i.transactiontypeid
      Where acctypeid = {acctypeid} and accuserid = {userid} and extract(month from purchasedate) = {month}"""
  )(for {
    itemId <- str("itemid")
    description <- str("description")
    value <- double("itemvalue")
    purchaseDate <- date("purchaseDate")
    trtType <- str("transactiontypename")
  } yield Item(itemId, description, value, purchaseDate, trtType))(
    'acctypeid -> acctypeId, 'userid -> userid, 'month -> month
  )
}
