package br.com.wallet.types

import play.api.libs.Codecs.sha1

/**
 * Created by sirkleber on 4/5/15.
 */
case class AccUser(accuserid: String, username: String, password: String) {
  def toLogin = AccUser(accuserid, username, sha1(password))

  def toInsert = AccUser(sha1(accuserid), username, sha1(password))
}
