package services.html

import com.github.novamage.svalidator.html.HtmlFormElementDecorator

object PlayBootstrap4HtmlFormElementDecorator extends HtmlFormElementDecorator {

  private val labelWidthClass = """col-xs-12 col-sm-2 col-lg-1"""
  private val controlsWidthClass = """col-xs-12 col-sm-8 col-md-5 col-lg-3"""

  override def decorateForm(formHtml: String,
                            attributes: Map[String, Any]): String = formHtml

  override def decorateFormBody(formBodyHtml: String,
                                attributes: Map[String, Any]): String = formBodyHtml

  override def decorateTextBox(inputHtml: String,
                               id: String,
                               name: String,
                               label: String,
                               errors: List[String],
                               attributes: Map[String, Any]): String = decorateBootstrapFormControl(inputHtml, id, label, errors)

  override def decoratePassword(inputHtml: String,
                                id: String,
                                name: String,
                                label: String,
                                errors: List[String],
                                attributes: Map[String, Any]): String = decorateBootstrapFormControl(inputHtml, id, label, errors)


  override def decorateCheckBox(inputHtml: String,
                                id: String,
                                name: String,
                                label: String,
                                errors: List[String],
                                attributes: Map[String, Any]): String = {
    s"""
       |<div class="form-group row">
       |<div class="$labelWidthClass">$label</div>
       |<div class="$controlsWidthClass">
       |<div class="custom-control custom-checkbox custom-control-inline">
       |$inputHtml
       |<label class="custom-control-label" for="$id"></label>
       |</div>
       |${ errorsHtmlFor(errors) }
       |</div>
       |</div>
       |""".stripMargin

  }

  override def decorateSelect(selectHtml: String,
                              id: String,
                              name: String,
                              label: String,
                              errors: List[String],
                              attributes: Map[String, Any]): String = decorateBootstrapFormControl(selectHtml, id, label, errors)

  override def decorateSelectOption(optionHtml: String,
                                    value: Any,
                                    text: Any,
                                    index: Int,
                                    attributes: Map[String, Any]): String = optionHtml


  override def decorateRadioGroup(radioGroupHtml: String,
                                  name: String,
                                  label: String,
                                  errors: List[String],
                                  attributes: Map[String, Any]): String = decorateBootstrapButtonGroup(radioGroupHtml, label, errors)

  override def decorateRadioGroupOption(radioHtml: String,
                                        id: String,
                                        name: String,
                                        label: String,
                                        attributes: Map[String, Any]): String = {
    s"""
       |<div class="custom-control custom-radio custom-control-inline">
       |$radioHtml
       |<label class="custom-control-label" for="$id">$label</label>
       |</div>
       |""".stripMargin
  }

  override def decorateCheckBoxGroup(checkBoxGroupHtml: String,
                                     name: String,
                                     label: String,
                                     errors: List[String],
                                     attributes: Map[String, Any]): String = decorateBootstrapButtonGroup(checkBoxGroupHtml, label, errors)

  override def decorateCheckBoxGroupOption(checkBoxHtml: String,
                                           id: String,
                                           name: String,
                                           label: String,
                                           attributes: Map[String, Any]): String = {
    s"""
       |<div class="custom-control custom-checkbox custom-control-inline">
       |$checkBoxHtml
       |<label class="custom-control-label" for="$id">$label</label>
       |</div>
       |""".stripMargin
  }

  override def decorateTextArea(textAreaHtml: String,
                                id: String,
                                name: String,
                                label: String,
                                errors: List[String],
                                attributes: Map[String, Any]): String = decorateBootstrapFormControl(textAreaHtml, id, label, errors)

  override def decorateButton(buttonHtml: String,
                              id: String,
                              name: String,
                              value: String,
                              attributes: Map[String, Any]): String = decorateButtonOrSubmit(buttonHtml)

  override def decorateSubmit(submitHtml: String,
                              id: String,
                              name: String,
                              value: String,
                              attributes: Map[String, Any]): String = decorateButtonOrSubmit(submitHtml)

  def errorsHtmlFor(errors: List[String]): String = {
    if (errors.isEmpty) {
      ""
    } else {
      val errorItems = errors.map(x => s"<li>$x</li>").mkString
      s"""
         |<div class="invalid-feedback">
         |<ul>$errorItems</ul>
         |</div>
      """.stripMargin
    }
  }

  private def decorateBootstrapFormControl(inputHtml: String,
                                           id: String,
                                           label: String,
                                           errors: List[String]): String = {
    s"""
       |<div class="form-group row">
       |<label for="$id" class="$labelWidthClass col-form-label">$label</label>
       |<div class="$controlsWidthClass">
       |$inputHtml
       |${ errorsHtmlFor(errors) }
       |</div>
       </div>""".stripMargin
  }

  private def decorateBootstrapButtonGroup(buttonGroupHtml: String,
                                           label: String,
                                           errors: List[String]): String = {
    s"""
       |<div class="form-group">
       |<div class="row">
       |<label class="col-form-label $labelWidthClass">$label</label>
       |<div class="$controlsWidthClass">
       |$buttonGroupHtml
       |${ errorsHtmlFor(errors) }
       |</div>
       |</div>
       |</div>
    """.stripMargin
  }

  private def decorateButtonOrSubmit(inputHtml: String): String = {
    s"""
       |<div class="form-group row">
       |<div class="$labelWidthClass"></div>
       |<div class="$controlsWidthClass">
       |$inputHtml
       |</div>
       |</div>
     """.stripMargin
  }


}
