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

