package models.forms

case class AddressForm(line1: String,
                       line2: Option[String],
                       city: String,
                       state: String,
                       zipCode: String) {

}
