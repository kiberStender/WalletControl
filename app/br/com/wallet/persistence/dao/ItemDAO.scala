package br.com.wallet.persistence.dao

import anorm.SqlParser._
import br.com.wallet.persistence.dto.Item

import scala.concurrent.Future

/**
 * Created by sirkleber on 13/08/15.
 */
object ItemDAO extends Dao {

  def getItemsByAccountType(acctypeId: String) = queryRunnerManyS(
    "Select * from item where acctype = {acctypeid}"
  )(for {
    itemId <- str("itemid")
    description <- str("description")
    purchaseDate <- date("purchaseDate")
    trtType <- str("trtype")
  } yield Item(itemId, description, purchaseDate, trtType))("acctypeid" -> acctypeId)
}
