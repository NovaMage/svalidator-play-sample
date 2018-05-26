package models.domain

import java.sql.Timestamp

import models.enumerations.Gender
import models.forms.AddressForm

case class Student(id: Long,
                   firstName: String,
                   lastName: String,
                   birthDate: Timestamp,
                   gender: Gender,
                   phone: String,
                   notes: Option[String],
                   address: AddressForm) {

}
