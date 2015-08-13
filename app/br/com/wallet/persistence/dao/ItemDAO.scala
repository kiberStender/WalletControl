package br.com.wallet.persistence.dao

import anorm.SqlParser._
import br.com.wallet.persistence.dto.Item

/**
 * Created by sirkleber on 13/08/15.
 */
object ItemDAO extends Dao {

  def getItemsByAccountType(acctypeId: String) = queryRunnerMany(
    "Select * from item where acctypeid = {acctypeid}"
  )(for {
    itemId <- str("itemid")
    description <- str("description")
    purchaseDate <- date("purchaseDate")
    trtType <- str("trtype")
  } yield Item(itemId, description, purchaseDate, trtType))("acctypeid" -> acctypeId)
}
