package br.com.wallet.types.actor

import akka.actor.{Props, ActorRef, Actor}
import br.com.wallet.persistence.dao.AccountTypeDAO
import br.com.wallet.types.transaction.request.SpreadsheetRequest
import play.api.libs.json.{Json, JsValue}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by sirkleber on 06/08/15.
 */
class SpreadsheetActor(out: ActorRef) extends Actor {
  override def receive: Receive = {
    case json: JsValue => handleJson(json)
  }

  private def handleJson: JsValue => Unit =  json => (json \ "requestType").asOpt[String] match {
    case Some("spreadsheet") => handleSpredsheetType((json \ "body").as[SpreadsheetRequest])
    case None => sender ! Json.obj("request" -> "No request type sent")
    case _ => sender ! Json.obj("request" -> "Uknown request type")
  }

  private def handleSpredsheetType: SpreadsheetRequest => Unit = req => for {
    acctype <- AccountTypeDAO getByUserId req.userid
  } yield out ! Json.obj("response" -> acctype)
}

object SpreadsheetActor {
  def props(out: ActorRef) = Props(new SpreadsheetActor(out))
}
