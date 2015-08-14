package br.com.wallet.persistence.dto

import play.api.libs.json.{Format, Json}

/**
 * Created by sirkleber on 01/08/15.
 */
case class Accuser(accuserid: String, usermail: String) extends Dto

object Accuser {
  implicit def formatter: Format[Accuser] = Json.format[Accuser]
}
