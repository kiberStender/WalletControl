package br.com.wallet.types

import com.github.nscala_time.time.Imports._

/**
 * Created by sirkleber on 4/5/15.
 */
trait AccountType {
  def acctypeid: String

  def description: String

  def balance: Double

  def closingday: DateTime
}