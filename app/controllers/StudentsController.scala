package controllers

import com.github.novamage.svalidator.validation.binding.BindingAndValidationSummary
import javax.inject.Inject
import models.enumerations.Gender
import models.forms.StudentCreateForm
import models.output.StudentCreateOutput
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents}
import services.StudentRepository

class StudentsController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) with I18nSupport {

  def index: Action[AnyContent] = Action { implicit request =>
    val students = StudentRepository.getAll
    Ok(views.html.student.index(students))
  }

  def create: Action[AnyContent] = Action { implicit request =>
    val genderOptions = Gender.values.sortBy(_.id).map(x => x.id -> request.messages(x.description))
    val output = StudentCreateOutput(BindingAndValidationSummary.empty[StudentCreateForm], genderOptions)
    Ok(views.html.student.create(output))
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    ???
  }

}
