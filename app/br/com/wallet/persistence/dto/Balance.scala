package br.com.wallet.persistence.dto

import com.github.nscala_time.time.Imports._

/**
 * Created by sirkleber on 13/08/15.
 */
case class Balance(balanceid: String, typeid : String, calcbalance: Double, realbalance: Double, balancedate: DateTime) extends Dto
