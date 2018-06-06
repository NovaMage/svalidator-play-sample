package services.html

import com.github.novamage.svalidator.html.{FormElementType, HtmlAttributeDecorator}

object PlayBootstrap4HtmlAttributeDecorator extends HtmlAttributeDecorator {

  override def decorateAttributes(elementType: FormElementType,
                                  attributes: Map[String, Any],
                                  errors: List[String]): Map[String, Any] = {
    import FormElementType._
    val elementSpecificAttrs = elementType match {
      case TextBox | Password | Select | TextArea => addClassToAttributes(attributes, "form-control")
      case RadioGroupOption | CheckBoxGroupOption | CheckBox => addClassToAttributes(attributes, "custom-control-input")
      case Button | Submit => addClassToAttributes(attributes, "btn btn-primary")
      case Form | Hidden | SelectOption => attributes
    }
    if(errors.nonEmpty){
      addClassToAttributes(elementSpecificAttrs, "is-invalid")
    } else {
      elementSpecificAttrs
    }
  }

  private def addClassToAttributes(attributes: Map[String, Any], classToAdd: String): Map[String, Any] = {
    val newClass = attributes.get("class").map(_.toString + " ").getOrElse("") + classToAdd
    attributes.updated("class", newClass)
  }

}
