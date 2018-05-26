package controllers

import javax.inject.Inject
import play.api.i18n.{I18nSupport, Lang}
import play.api.mvc._

class HomeController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) with I18nSupport {

  def index: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.home.index())
  }

  def setEnglishLanguage(): Action[AnyContent] = Action { implicit request =>
    Redirect(routes.HomeController.index()).withLang(Lang("en"))
  }

  def setSpanishLanguage(): Action[AnyContent] = Action { implicit request =>
    Redirect(routes.HomeController.index()).withLang(Lang("es"))
  }

}
