package br.com.wallet.api.controller

import br.com.wallet.types.oauthUser.OAuthUser
import play.api.libs.json.Json
import akka.util.Timeout
import br.com.wallet.api.models.result.Failure
import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by sirkleber on 4/7/15.
 */
trait ActionController extends Controller {
  protected implicit lazy val timeout = Timeout(3 second)

  protected def jsonApp = "application/json"

  protected def oauthUserSession = "oauth_user"
  protected def oauthStateSesion = "oauth_state"

  protected def validateSession: (String, String) => ((OAuthUser, Request[AnyContent]) => Future[Result]) => Action[AnyContent] = {
    case (state, userid) => f => Action.async {req =>
      req.session.get(oauthUserSession) match {
        case Some(userJson) => (for {
          user <- Json.parse(userJson).validate[OAuthUser]
        } yield if(user.state == state){
            f(user, req)
          } else {
            Future(Ok(Failure("Sessão expirou") toJson) as jsonApp)
          }).getOrElse(Future(Ok(Failure("Erro ao processar dados do usuário") toJson) as jsonApp))
        case None => Future{Ok(Failure("Sessão expirou") toJson) as jsonApp}
      }
    }
  }
}