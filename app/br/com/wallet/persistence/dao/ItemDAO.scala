package br.com.wallet.persistence.dao

import anorm.SqlParser._
import br.com.wallet.persistence.dto.Item

import scala.concurrent.Future

/**
 * Created by sirkleber on 13/08/15.
 */
object ItemDAO extends Dao {

  def insertItem: (String, String, Item) => Future[Unit] = {
    case (acctypeid, accuserid, Item(id, desc, value, date, typeid)) => queryUpdate(
      """Insert into item(itemid, transactiontypeid, acctypeid, accuserid, description, itemvalue, purchasedate) values(
          {id}, {trtypeid}, {acctypeid}, {accuserid}, {desc}, {vl}, {purchasedt}
         )"""
    )(
      'id -> id, 'trtypeid -> typeid, 'acctypeid -> acctypeid,'accuserid -> accuserid, 'desc -> desc,
      'vl -> value, 'purchasedt -> date
    )
  }

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
