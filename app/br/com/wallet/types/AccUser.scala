package br.com.wallet.types

import play.api.libs.Codecs.sha1
import play.api.libs.json.Format
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Created by sirkleber on 4/5/15.
 */
case class AccUser(accuserid: String, username: String, password: String) {
  def toLogin = AccUser(accuserid, username, sha1(password))

  def toInsert = AccUser(sha1(accuserid), username, sha1(password))
}

object AccUser {
  implicit def formatter: Format[AccUser] = (
    (__ \ "accuserid").format[String] ~
    (__ \ "username").format[String] ~
    (__ \ "password").format[String]
  )(AccUser.apply, unlift(AccUser.unapply))
}
