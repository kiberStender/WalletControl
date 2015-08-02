package br.com.wallet.persistence.dao

import play.api.db.DB
import anorm.{NamedParameter, RowParser, SQL}
import play.api.Play.current
import java.sql.Connection
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by sirkleber on 4/6/15.
 */
trait Dao {
  protected def queryRunnerSingle[T](query: String)(parser: RowParser[T])(prmts: NamedParameter*): Future[T] = Future {
    DB.withConnection {implicit conn => SQL(query).on(prmts: _*).as(parser.single)}
  }

  protected def queryRunnerMany[T](query: String)(parser: RowParser[T])(prmts: NamedParameter*): Future[List[T]] = Future {
    DB.withConnection {implicit conn => SQL(query).on(prmts: _*).as(parser *)}
  }

  protected def queryUpdate(query: String)(prmts: NamedParameter*): Future[Unit] = Future {
    DB.withConnection{ implicit conn => SQL(query).on(prmts: _*).executeUpdate}
  }
}
