package br.com.wallet.types

import com.github.nscala_time.time.Imports._
import play.api.libs.Codecs.{sha1}

/**
 * Created by sirkleber on 4/5/15.
 */
trait AccountType {
  def acctypeid: String = closingday.toString split 'T' match {
    case Array(date, _) => sha1(s"$date|$description|${DateTime.now}")
  }

  def description: String

  def balance: Double

  def closingday: DateTime
}