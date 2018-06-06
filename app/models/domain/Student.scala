package models.domain

import java.sql.Timestamp

import models.enumerations.Gender

case class Student(id: Long,
                   firstName: String,
                   lastName: String,
                   birthDate: Timestamp,
                   gender: Gender,
                   phone: String,
                   foreigner: Boolean,
                   notes: Option[String],
                   address: Address) {

}
