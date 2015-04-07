package br.com.wallet.api.models.result

import play.api.libs.json.Format
import play.api.libs.json.Json

trait Result[A] {
  def failed: Boolean
  def result: Option[A]
  def description: String
  
  def toJson(implicit formatter: Format[A]) = Json.obj("failed" -> failed, "description" -> description, "result" -> (result match {
    case None => ""
    case Some(a) => a
  }))
}

case class Failure(des: String) extends Result[String]{
  def failed = true
  def result = None
  def description = des
}

case class Success[A](res: A) extends Result[A]{
  def failed = false
  def result = Some(res)
  def description = ""
}