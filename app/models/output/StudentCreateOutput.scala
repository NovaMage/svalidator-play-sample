package models.output

import com.github.novamage.svalidator.validation.binding.BindingAndValidationSummary
import models.forms.StudentCreateForm

case class StudentCreateOutput(summary: BindingAndValidationSummary[StudentCreateForm],
                               genderOptions: List[(Int, String)])  {

}
