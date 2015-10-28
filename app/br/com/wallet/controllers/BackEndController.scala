package br.com.wallet.controllers

import java.util.UUID

import br.com.wallet.api.controller.ActionController
import br.com.wallet.api.models.result.{Failure, Success}
import br.com.wallet.persistence.dao.{BalanceDao, ItemDAO, AccountTypeDAO}
import br.com.wallet.persistence.dto.Item
import br.com.wallet.types.loginOption.LoginOption
import br.com.wallet.types.loginTypes.{GithubType, LoginType, GoogleType}
import br.com.wallet.types.oauthUser.OAuthUser
import play.api.libs.Codecs
import play.api.libs.json.Json
import play.api.{Configuration, Play}
import play.api.mvc.Action
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current
import scala.language.postfixOps

import scala.concurrent.Future

/**
 * Created by sirkleber on 29/06/15.
 */
class BackEndController extends ActionController {

  private lazy val loginList: List[LoginType] = {
    lazy val conf: Configuration = Play.current.configuration
    List(GoogleType(conf), GithubType(conf))
  }

  def auth = Action.async { implicit req =>
    lazy val callbackUrl = routes.BackEndController.login _

    def list(state: String): List[LoginOption] = (for {
      ltype <- loginList
    } yield ltype.authData((state, callbackUrl))).filter(_.isDefined).map(_.get)

    Future {
      req.session.get(oauthStateSesion) match {
        case Some(token) => Ok(Success(list(token)) toJson) as jsonApp
        case None =>
          lazy val state: String = UUID.randomUUID().toString
          Ok(Success(list(state)) toJson).withSession(oauthStateSesion -> state) as jsonApp
      }
    }
  }

  def login(provider: String, codeOpt: Option[String], stateOpt: Option[String]) = Action.async { implicit req =>
    (for {
      loginType <- loginList find (_.provider == provider)
      code <- codeOpt
      state <- stateOpt
      oauthState <- req.session.get(oauthStateSesion)
    } yield if (state == oauthState) {
        for {
          fToken <- loginType.getToken(code, routes.BackEndController.login(loginType.provider, None, None).absoluteURL())
        } yield (for {
          (token, user) <- fToken
        } yield {
            def json = Json.toJson(OAuthUser(state, code, user, token, loginType))
            Redirect("/spreadsheet").withSession(oauthUserSession -> json.toString)
          }
          ).getOrElse(BadRequest("Provedor não autorizou"))
      } else {
        Future.successful(BadRequest("Você não está logado"))
      }
      ) getOrElse Future.successful(BadRequest("Servidor não proveu os valores"))
  }

  def getSpreadsheetData(state: String, userid: String) = validateSession(state, userid) { (_, _) => for {
    acctype <- AccountTypeDAO getByUserId userid
  } yield Ok(Success(acctype) toJson) as jsonApp
  }

  private def acctypeid = "5cfb525a5c5a5dd29beb6f257b6a7f22d157c630"

  def insertItem(state: String, userid: String) = validateSession(state, userid) { (user, req) =>
    lazy val action = for {
      itemJs <- req.body.asJson
    } yield for {
        item <- itemJs.validate[Item]
      } yield for {
          itemId <- Future(Codecs.sha1(s"$userid-${item.purchaseDate}-item"))
          _ <- ItemDAO insertItem(acctypeid, userid, item.withItemId(itemId))
        } yield (Success("Ok"), item.value)

    action match {
      case Some(vl) => vl.asOpt match {
        case Some(ft) => for {
          (result, balance) <- ft
          _ <- BalanceDao.updateOrInsert(balance, user.logonData.usermail, acctypeid, userid)
        } yield Ok(result toJson) as jsonApp
        case None => Future(Ok(Failure("Problema ao inserir novo item") toJson) as jsonApp)
      }
      case None => Future(Ok(Failure("Json Inválido").toJson) as jsonApp)
    }
  }
}