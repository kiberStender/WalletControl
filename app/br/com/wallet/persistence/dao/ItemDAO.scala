package br.com.wallet.persistence.dao

import anorm.SqlParser._
import br.com.wallet.persistence.dto.Item

/**
 * Created by sirkleber on 13/08/15.
 */
object ItemDAO extends Dao {

  def getItemsByAccountType(acctypeId: String) = queryRunnerManyS(
    """Select itemid, transactiontypename, description, itemvalue, purchasedate
      From item i join transactiontype t on t.transactiontypeid = i.transactiontypeid
      Where acctypeid = {acctypeid}"""
  )(for {
    itemId <- str("itemid")
    description <- str("description")
    value <- double("itemvalue")
    purchaseDate <- date("purchaseDate")
    trtType <- str("transactiontypename")
  } yield Item(itemId, description, value, purchaseDate, trtType))("acctypeid" -> acctypeId)
}
