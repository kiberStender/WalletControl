package br.com.wallet.persistence.dao

import br.com.wallet.persistence.dto.Accuser

import anorm.SqlParser._

/**
 * Created by sirkleber on 4/6/15.
 */
object AccuserDAO extends Dao {

  def getAccusers(usermail: String) = queryRunnerSingle(
    "Select accuserid from accuser where usermail = {usermail}"
  )(str("accuserid"))("usermail" -> usermail)

  def insertAccuser(useracc: Accuser) = useracc match {
    case Accuser(id, mail) =>
      lazy val query = "Insert into accuser(accuserid, usermail) values({accuserid}, {usermail})"
      queryUpdate(query)("accuserid" -> id, "usermail" -> mail)
  }
}
