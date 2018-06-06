package models.output

import com.github.novamage.svalidator.validation.binding.BindingAndValidationSummary
import models.forms.StudentUpdateForm

case class StudentUpdateOutput(id: Long,
                               summary: BindingAndValidationSummary[StudentUpdateForm],
                               genderOptions: List[(Int, String)]) {

}

