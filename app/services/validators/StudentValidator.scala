package services.validators

import java.sql.Timestamp
import java.time.LocalDateTime

import com.github.novamage.svalidator.validation.ValidationSummary
import com.github.novamage.svalidator.validation.simple.SimpleValidator
import javax.inject.Inject
import models.forms.StudentForm

/**
  * This validator holds common validation for both create and update operations.
  *
  * @param addressValidator The secondary validator to delegate validation of the address component field
  */
class StudentValidator @Inject()(addressValidator: AddressValidator) extends SimpleValidator[StudentForm] {

  override def validate(implicit instance: StudentForm): ValidationSummary = {
    val maxCharsForNameFields = 32
    val maxStudentAge = 18
    val maxCharsForNotes = 512
    WithRules(
      For { _.firstName } ForField 'firstName
        must { _.length <= maxCharsForNameFields } withFormattedMessage("validation.general.maxCharactersExceeded", maxCharsForNameFields),
      For { _.lastName } ForField 'lastName
        must { _.length <= maxCharsForNameFields } withFormattedMessage("validation.general.maxCharactersExceeded", maxCharsForNameFields),
      For { _.birthDate } ForField 'birthDate
        mustNot beOlderThan(maxStudentAge) withFormattedMessage("validation.student.studentTooOld", maxStudentAge),
      For { _.phone } ForField 'phone
        must beAValidPhone withMessage "validation.general.invalidPhone",
      ForOptional { _.notes } ForField 'notes
        must { _.length <= maxCharsForNotes } withFormattedMessage("validation.general.maxCharactersExceeded", maxCharsForNameFields),
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
