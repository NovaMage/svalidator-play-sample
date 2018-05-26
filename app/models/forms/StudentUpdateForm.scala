package models.forms

import java.sql.Timestamp

import models.enumerations.Gender

case class StudentUpdateForm(id: Long,
                             firstName: String,
                             lastName: String,
                             birthDate: Timestamp,
                             gender: Gender,
                             phone: String,
                             notes: Option[String],
                             address: AddressForm) {

}
