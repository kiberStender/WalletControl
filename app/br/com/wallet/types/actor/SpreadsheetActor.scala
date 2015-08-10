package br.com.wallet.types.actor

import akka.actor.{Props, ActorRef, Actor}
import play.api.libs.json.{Json, JsValue}

/**
 * Created by sirkleber on 06/08/15.
 */
class SpreadsheetActor(out: ActorRef) extends Actor{
  override def receive: Receive = {
    case json: JsValue => handleJson(json)

  }

  private def handleJson: JsValue => Unit =  json => (json \ "requestType").asOpt[String] match {
    case Some("SpreadsheetRequest") => sender ! Json.obj("request" -> "Spreadsheet request came")
    case None => sender ! Json.obj("request" -> "No request type sent")
    case _ => sender ! Json.obj("request" -> "Uknown request type")
  }
}

object SpreadsheetActor {
  def props(out: ActorRef) = Props(new SpreadsheetActor(out))
}
