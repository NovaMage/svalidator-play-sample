package models.forms

import java.sql.Timestamp

import models.domain.Address
import models.enumerations.Gender

case class StudentUpdateForm(id: Long,
                             firstName: String,
                             lastName: String,
                             birthDate: Timestamp,
                             gender: Gender,
                             phone: String,
                             foreigner: Boolean,
                             notes: Option[String],
                             address: Address) extends StudentForm {

}
