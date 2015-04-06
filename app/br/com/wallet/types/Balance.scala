package br.com.wallet.types

import com.github.nscala_time.time.Imports._

/**
 * Created by sirkleber on 4/6/15.
 */
case class Balance(balanceid: String, typeid : String, calcbalance: Double, realbalance: Double, balancedate: DateTime)
