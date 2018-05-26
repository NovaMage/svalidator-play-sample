package models.forms

import java.sql.Timestamp

import models.enumerations.Gender

case class StudentCreateForm(firstName: String,
                             lastName: String,
                             birthDate: Timestamp,
                             gender: Gender,
                             phone: String,
                             notes: Option[String],
                             address: AddressForm) {

}
