package br.com.wallet.persistence.dto

import play.api.libs.json.{Format, Json}

/**
 * Created by sirkleber on 01/08/15.
 */
case class AccuserDto(accuserid: String, usermail: String) extends Dto

object AccuserDto {
  implicit def formatter: Format[AccuserDto] = Json.format[AccuserDto]
}
