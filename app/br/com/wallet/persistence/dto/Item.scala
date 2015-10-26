package br.com.wallet.persistence.dto

import java.util.Date

import play.api.libs.json.{Json, Format}

/**
 * Created by sirkleber on 13/08/15.
 */
case class Item(
  itemId: String, description: String, value: Double, purchaseDate: Date, trtType: String
) extends Dto {
  override def toString =
    s"{itemId: $itemId, description: $description, value: $value, purchaseDate: $purchaseDate, trtType: $trtType}"

  def withItemId(itemId_ : String): Item = Item(itemId_, description, value, purchaseDate, trtType)
}

object Item {
  implicit def formatter: Format[Item] = Json.format[Item]
}
