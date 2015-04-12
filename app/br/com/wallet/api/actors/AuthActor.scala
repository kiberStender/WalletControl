package br.com.wallet.api.actors

import akka.actor.Actor
import br.com.wallet.persistence.dao.AccUserDAO
import br.com.wallet.types.AccUser

/**
 * Created by sirkleber on 4/7/15.
 */
class AuthActor extends Actor {
  lazy val dao: AccUserDAO = new AccUserDAO

  def receive = {
    case accUser: AccUser => self ! dao.login(accUser toLogin)
  }
}
