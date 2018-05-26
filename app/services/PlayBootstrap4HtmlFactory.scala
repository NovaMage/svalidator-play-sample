package services

import com.github.novamage.svalidator.html.HtmlFactory
import play.twirl.api.Html

object PlayBootstrap4HtmlFactory extends HtmlFactory(Html.apply) {

  implicit def factory: HtmlFactory[Html] = this

}
