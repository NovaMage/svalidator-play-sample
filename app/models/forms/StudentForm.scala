package models.forms

import java.sql.Timestamp

import models.domain.Address
import models.enumerations.Gender

trait StudentForm {

  def firstName: String

  def lastName: String

  def birthDate: Timestamp

  def gender: Gender

  def phone: String

  def foreigner: Boolean

  def notes: Option[String]

  def address: Address

}
