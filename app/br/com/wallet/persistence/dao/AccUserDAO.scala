package br.com.wallet.persistence.dao

import br.com.wallet.types.AccUser

import anorm.{Row, SQL}
import anorm.SqlParser._
import play.api.db.{DB}
import play.api.Play.current
import java.sql.Connection

/**
 * Created by sirkleber on 4/6/15.
 */
class AccUserDAO extends AbstractDAO {

  def login(accUser: AccUser): Option[AccUser] = accUser match {
    case AccUser(_, username, password) => DB.withConnection { implicit  conn =>
      SQL("Select * From accuser where username = {username} and password = {password}")
        .on("username" -> username, "password" -> password)
        .as ((for {
          id <- str("accuserid")
          username <- str("username")
          pass <- str("password")
        } yield AccUser(id, username, pass)).singleOpt)
      } headOption
    }
  }
