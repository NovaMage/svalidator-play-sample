package models.output

import com.github.novamage.svalidator.validation.binding.BindingAndValidationWithData
import models.forms.StudentCreateForm

case class StudentCreateOutput(summary: BindingAndValidationWithData[StudentCreateForm, List[Any]],
                               genderOptions: List[(Int, String)])  {

}
