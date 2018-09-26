package services.validators

import com.github.novamage.svalidator.play.binding.PlayBindingValidatorWithData
import com.github.novamage.svalidator.validation.ValidationWithData
import javax.inject.Inject
import models.forms.StudentUpdateForm
import services.repositories.StudentRepository

class StudentUpdateValidator @Inject()(studentValidator: StudentValidator) extends PlayBindingValidatorWithData[StudentUpdateForm, List[Any]] {

  override def validate(implicit instance: StudentUpdateForm): ValidationWithData[List[Any]] = {
    /*
    If this repository required db access, it would be preferable to avoid hitting the database twice,
    so we check existence only once before giving the error to both fields
    Another alternative is to give the error only once to a field with different name that represents the combination
    of both
    */
    val studentAlreadyExists = StudentRepository.hasWithNamesWithOtherId(instance.id, instance.firstName, instance.lastName)
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
