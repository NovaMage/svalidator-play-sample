package services.html

import com.github.novamage.svalidator.html.HtmlFactory
import play.api.mvc.{Call, Request}
import play.twirl.api.Html
import views.html.helper

object PlayBootstrap4HtmlFactory extends HtmlFactory(converter = Html.apply,
  inputDecorator = PlayBootstrap4HtmlFormElementDecorator,
  attributeDecorator = PlayBootstrap4HtmlAttributeDecorator) {

  implicit def factory: PlayBootstrap4HtmlFactory.type = this

  def form(action: Call,
           enctype: String = "application/x-www-form-urlencoded",
           attributes: Map[String, Any] = Map.empty)(body: => Html)(implicit request: Request[_]): Html = {
    val csrf = helper.CSRF.formField
    super.form(action.url, action.method.toUpperCase, enctype, attributes)(csrf.toString() + body.toString)
  }

}
