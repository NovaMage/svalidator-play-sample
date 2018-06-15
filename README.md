svalidator-play-sample
====================
A play scala application showcasing the use of the SValidator library integration

The application has minimal configuration, and only uses the webjar for the Bootstrap framework to 
demonstrate the usage of SValidator for decoration of inputs.  Also, an HTML factory specifically tailored to Play! is
implemented to show how to integrate everything with Play's routes and CSRF filter.

Tour of points of interest in this application
=========================================

In application.conf, we set up a module to initialize our configurations with guice

```hocon
  play.modules.enabled += infrastructure.GuiceConfigurationModule
```

In the module itself, we configure an eager binding to set up initialization of svalidator, in the way that the 
[documentation of play framework recommends](https://www.playframework.com/documentation/2.6.x/GlobalSettings).

```scala
class GuiceConfigurationModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[StartUpConfigurator]).asEagerSingleton()
  }

}
```

In the configurator itself, we initialize the binders of svalidator, and also configure to allow recursive binding for
type `Address`, since we won't write a custom binder for it.  We also initialize binding language configuration to 
return keys that can be localized for binding errors.

```scala
import com.github.novamage.svalidator.binding.{BindingConfig, TypeBinderRegistry}
import models.domain.Address

class StartUpConfigurator {

  TypeBinderRegistry.initializeBinders(new BindingConfig(dateFormat = StartUpConfigurator.dateFormat, languageConfig = ApplicationBindingLanguageConfig))
  TypeBinderRegistry.allowRecursiveBindingForType[Address]()

}

object StartUpConfigurator {

  val dateFormat = "dd/MM/yyyy"
}
```

That sets up binding.  Now, for the html helpers to work in the desired way, it's necessary to implement an HTML that 
fits our intended setup, in this case, Bootstrap 4.

```scala
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
```

We create an `HTMLFactory` with a `converter` that transforms all results into Play's `Html` class, thus making it 
output properly in Twirl templates.  We provide custom input and attribute decorators to add the proper classes for 
styling according to Bootstrap 4 specifications.  Be sure to check the source code of the decorators for more insight on
how the classes are added.

We added a `form` method, that receives Play's `Call` as the action, simplifying the way forms are created and making 
forms more consistent as they take the method directly from Play's routes rather than having to pass a `String` 
argument.  In addition, we automatically build the csrf form field and put it into the form.

That's basically it as far as it goes in terms of initialization and infrastructure.  From there on, it's all about
building your specific application using the library.  In this case, we chose to make a simple application that allows 
us to create, update, and list students.  It uses a very simple storage that saves the list of students in a map in
memory.  This approach was chosen to avoid the unneeded complexity of setting other kinds of persistent storage.

The main domain class is the `Student` class, alongside the `Address` class:

```scala
package models.domain

import java.sql.Timestamp
import models.enumerations.Gender

case class Student(id: Long,
                   firstName: String,
                   lastName: String,
                   birthDate: Timestamp,
                   gender: Gender,
                   phone: String,
                   foreigner: Boolean,
                   notes: Option[String],
                   address: Address)
                   
case class Address(line1: String,
                   line2: Option[String],
                   city: String,
                   state: String,
                   zipCode: String)
```

We create form classes that pretty much match the domain class, except that when a student is created, its id is not 
provided by the form.  For the address, we use the domain model directly for binding and validation, so no form is 
needed.  Both approaches are perfectly valid (using a separate form object or using the domain model directly for 
binding), so it will be up to you to decide what you need.  Naturally, if some form fields do not exist in your domain 
object, you will be forced to use a separate form class.

```scala
package models.forms

import java.sql.Timestamp
import models.domain.Address
import models.enumerations.Gender

trait StudentForm {
  def firstName: String
  def lastName: String
  def birthDate: Timestamp
  def gender: Gender
  def phone: String
  def foreigner: Boolean
  def notes: Option[String]
  def address: Address
}

case class StudentCreateForm(firstName: String,
                             lastName: String,
                             birthDate: Timestamp,
                             gender: Gender,
                             phone: String,
                             foreigner: Boolean,
                             notes: Option[String],
                             address: Address) extends StudentForm
                             
case class StudentUpdateForm(id: Long,
                          firstName: String,
                          lastName: String,
                          birthDate: Timestamp,
                          gender: Gender,
                          phone: String,
                          foreigner: Boolean,
                          notes: Option[String],
                          address: Address) extends StudentForm
```

We extend the common `StudentForm` trait to allow us to pass a `BindingAndValidationSummary[StudentForm]` to our shared
form template that will be used for both the `create` and `update` actions.  `BindingAndValidationSumary` is covariant 
on its type argument therefore summaries of classes that extend the type argument will also be allowed in place.

Now, the views of our forms:

```html
 <!-- create.scala.html -->
@(model: models.output.StudentCreateOutput)(implicit request: MessagesRequest[_])
@import services.html.PlayBootstrap4HtmlFactory.factory

@defining(request.messages) { t =>
    @shared.main(t("student.create.title")) {
        <p>
            <a class="btn btn-primary" href="@routes.StudentsController.index()">
            @t("student.backToIndex")
            </a>
        </p>
        @factory.form(routes.StudentsController.postCreate()) {
            @student.form(model.summary, model.genderOptions, "student.create.createStudent", t)
        }
    }
}
```
```html
<!-- update.scala.html -->
@(model: models.output.StudentUpdateOutput)(implicit request: MessagesRequest[_])
@import services.html.PlayBootstrap4HtmlFactory.factory
@import com.github.novamage.svalidator.html.BindingAndValidationSummaryHelper.helper

@defining(request.messages) { t =>
    @shared.main(t("student.update.title")) {
        <p>
            <a class="btn btn-primary" href="@routes.StudentsController.index()">
            @t("student.backToIndex")
            </a>
        </p>
        @factory.form(routes.StudentsController.postUpdate(model.id)) {
            @model.summary.hidden("id", _.id)
            @student.form(model.summary, model.genderOptions, "student.update.updateStudent", t)
        }
    }
}
```

Our form pages are pretty simple, as they mostly reuse the entire form as a partial, called above with the `@student.form` 
and passing in the appropriate options.  Now, for the form itself:

```html
@import com.github.novamage.svalidator.validation.binding.BindingAndValidationSummary
@import models.forms.StudentForm
@import com.github.novamage.svalidator.html.BindingAndValidationSummaryHelper.helper
@import services.html.PlayBootstrap4HtmlFactory.factory
@(summary: BindingAndValidationSummary[StudentForm], genderOptions: List[(Any, Any)], submitKey: String, t: Messages)

@summary.textBox("firstName", _.firstName, t("student.attributes.firstName"))
@summary.textBox("lastName", _.lastName, t("student.attributes.lastName"))
@summary.textBox("birthDate", _.birthDate, t("student.attributes.birthDate"))
@summary.radioGroup("gender", _.gender, genderOptions, t("student.attributes.gender"))
@summary.textBox("phone", _.phone, t("student.attributes.phone"))
@summary.checkBox("foreigner", _.foreigner, t("student.attributes.foreigner"))
@summary.textArea("notes", _.notes, t("student.attributes.notes"))
@summary.textBox("address.line1", _.address.line1, t("student.attributes.addressLine1"))
@summary.textBox("address.line2", _.address.line2, t("student.attributes.addressLine2"))
@summary.textBox("address.city", _.address.city, t("student.attributes.addressCity"))
@summary.textBox("address.state", _.address.state, t("student.attributes.addressState"))
@summary.textBox("address.zipCode", _.address.zipCode, t("student.attributes.addressZipCode"))
@summary.submit("createStudent", t(submitKey))
```

This generates some neat looking html inputs that will be further decorated if errors need to be shown.

When the form is posted, it is bound and validated on the controller by calling its specific validator.  For the 
`create` action:

```scala
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
```

If the binding and validation is successful, we create the student and redirect with a success message.  If it fails,
we re-render the form with the failing summary.  The validator applied here looks like this:

```scala
package services.validators

import com.github.novamage.svalidator.play.binding.PlayBindingValidator
import com.github.novamage.svalidator.validation.ValidationSummary
import javax.inject.Inject
import models.forms.StudentCreateForm
import services.repositories.StudentRepository

class StudentCreateValidator @Inject()(studentValidator: StudentValidator) extends PlayBindingValidator[StudentCreateForm] {

  override def validate(implicit instance: StudentCreateForm): ValidationSummary = {
    /*
    If this repository required db access, it would be preferable to avoid hitting the database twice,
    so we check existence only once before giving the error to both fields
    Another alternative is to give the error only once to a field with different name that represents the combination
    of both
    */
    val studentAlreadyExists = StudentRepository.hasWithNames(instance.firstName, instance.lastName)
    studentValidator.validate.merge(
      WithRules(
        For { _ => studentAlreadyExists } ForField 'firstName
          mustNot identity withMessage "validation.student.studentAlreadyRegistered",
        For { _ => studentAlreadyExists } ForField 'lastName
          mustNot identity withMessage "validation.student.studentAlreadyRegistered"
      )
    )
  }

}
```

To reuse common logic, we encapsulate validation logic that applies on both `create` and `update` into a common 
validator `StudentValidator`, and validate `create` specific stuff here.  The `StudentValidator` then looks like this:

```scala
package services.validators

import java.sql.Timestamp
import java.time.LocalDateTime
import com.github.novamage.svalidator.validation.ValidationSummary
import com.github.novamage.svalidator.validation.simple.SimpleValidator
import javax.inject.Inject
import models.forms.StudentForm

class StudentValidator @Inject()(addressValidator: AddressValidator) extends SimpleValidator[StudentForm] {

  override def validate(implicit instance: StudentForm): ValidationSummary = {
    val maxCharsForNameFields = 32
    val maxStudentAge = 18
    val maxCharsForNotes = 512
    WithRules(
      For { _.firstName } ForField 'firstName
        must { _.length <= maxCharsForNameFields } withMessage("validation.general.maxCharactersExceeded", maxCharsForNameFields),
      For { _.lastName } ForField 'lastName
        must { _.length <= maxCharsForNameFields } withMessage("validation.general.maxCharactersExceeded", maxCharsForNameFields),
      For { _.birthDate } ForField 'birthDate
        mustNot beOlderThan(maxStudentAge) withMessage("validation.student.studentTooOld", maxStudentAge),
      For { _.phone } ForField 'phone
        must beAValidPhone withMessage "validation.general.invalidPhone",
      ForOptional { _.notes } ForField 'notes
        must { _.length <= maxCharsForNotes } withMessage("validation.general.maxCharactersExceeded", maxCharsForNameFields),
      ForComponent { _.address } ForField 'address
        validateUsing addressValidator


    )
  }

  private def beOlderThan(maxStudentAge: Int): Timestamp => Boolean = { dateTime =>
    LocalDateTime.now().getYear - dateTime.toLocalDateTime.getYear > maxStudentAge
  }

  private def beAValidPhone(phone: String): Boolean = {
    phone.length == 10 && phone.forall(_.isDigit)
  }
}
```

Address validation is delegated to a separate validator that validates that specific component only.  For all fields 
validated, error keys are associated and arguments passed in so that they may be formatted with them.

And that is the conclusion of our tour.  Be sure to check the source code if you'd like to check further, 
or just run the application to see it all in action.
