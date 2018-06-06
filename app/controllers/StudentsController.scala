package controllers

import com.github.novamage.svalidator.validation.binding.{BindingAndValidationSummary, Failure, Success}
import javax.inject.Inject
import models.enumerations.Gender
import models.forms.{StudentCreateForm, StudentUpdateForm}
import models.output.{StudentCreateOutput, StudentUpdateOutput}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents}
import services.repositories.StudentRepository
import services.validators.{StudentCreateValidator, StudentUpdateValidator}

class StudentsController @Inject()(cc: MessagesControllerComponents,
                                   studentCreateValidator: StudentCreateValidator,
                                   studentUpdateValidator: StudentUpdateValidator) extends MessagesAbstractController(cc) with I18nSupport {

  def index: Action[AnyContent] = Action { implicit request =>
    val students = StudentRepository.getAll
    Ok(views.html.student.index(students))
  }

  def create: Action[AnyContent] = Action { implicit request =>
    val genderOptions = Gender.values.sortBy(_.id).map(x => x.id -> request.messages(x.description))
    val output = StudentCreateOutput(BindingAndValidationSummary.empty[StudentCreateForm], genderOptions)
    Ok(views.html.student.create(output))
  }

  def postCreate: Action[AnyContent] = Action { implicit request =>
    val summary = studentCreateValidator.bindLocalized
    summary match {
      case Success(form) =>
        StudentRepository.create(form)
        Redirect(routes.StudentsController.index()).flashing("message" -> request.messages("student.create.createdSuccessfully"))
      case Failure(_) =>
        val genderOptions = Gender.values.sortBy(_.id).map(x => x.id -> request.messages(x.description))
        val output = StudentCreateOutput(summary, genderOptions)
        Ok(views.html.student.create(output))
    }
  }

  def update(id: Long): Action[AnyContent] = Action { implicit request =>
    val genderOptions = Gender.values.sortBy(_.id).map(x => x.id -> request.messages(x.description))
    val studentOption = StudentRepository.get(id)
    val formOption = studentOption.map(x => StudentUpdateForm(x.id, x.firstName, x.lastName, x.birthDate, x.gender, x.phone, x.foreigner, x.notes, x.address))
    val summary = formOption.map(BindingAndValidationSummary.filled).getOrElse(BindingAndValidationSummary.empty[StudentUpdateForm])
    val output = StudentUpdateOutput(id, summary, genderOptions)
    Ok(views.html.student.update(output))
  }

  def postUpdate(id: Long): Action[AnyContent] = Action { implicit request =>
    val summary = studentUpdateValidator.bindLocalized
    summary match {
      case Success(form) =>
        StudentRepository.update(form)
        Redirect(routes.StudentsController.index()).flashing("message" -> request.messages("student.update.updatedSuccessfully"))
      case Failure(_) =>
        val genderOptions = Gender.values.sortBy(_.id).map(x => x.id -> request.messages(x.description))
        val output = StudentUpdateOutput(id, summary, genderOptions)
        Ok(views.html.student.update(output))
    }
  }

}
