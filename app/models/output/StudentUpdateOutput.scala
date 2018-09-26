package models.output

import com.github.novamage.svalidator.validation.binding.BindingAndValidationWithData
import models.forms.StudentUpdateForm

case class StudentUpdateOutput(id: Long,
                               summary: BindingAndValidationWithData[StudentUpdateForm, List[Any]],
                               genderOptions: List[(Int, String)]) {

}

