package models.domain

case class Address(line1: String,
                   line2: Option[String],
                   city: String,
                   state: String,
                   zipCode: String) {

}
