package br.com.wallet.persistence.dto

import java.util.Date

import play.api.libs.json.{Json, Format}

/**
 * Created by sirkleber on 13/08/15.
 */
case class Item(itemId: String, description: String, purchaseDate: Date, trtType: String) extends Dto

object Item {
  implicit def formatter: Format[Item] = Json.format[Item]
}
