package services.validators

import com.github.novamage.svalidator.validation.ValidationWithData
import com.github.novamage.svalidator.validation.simple.SimpleValidator
import models.domain.Address

class AddressValidator extends SimpleValidator[Address] {

  override def validate(implicit instance: Address): ValidationWithData[Nothing] = {
    val maxCharsForStreetFields = 64
    val maxCharsForCityAndState = 32
    WithRules(
      For { _.line1 } ForField 'line1
        must { _.length <= maxCharsForStreetFields } withMessage("validation.general.maxCharactersExceeded", maxCharsForStreetFields),
      ForOptional { _.line2 } ForField 'line2
        must { _.length <= maxCharsForStreetFields } withMessage("validation.general.maxCharactersExceeded", maxCharsForStreetFields),
      For { _.city } ForField 'city
        must { _.length <= maxCharsForCityAndState } withMessage("validation.general.maxCharactersExceeded", maxCharsForCityAndState),
      For { _.state } ForField 'city
        must { _.length <= maxCharsForCityAndState } withMessage("validation.general.maxCharactersExceeded", maxCharsForCityAndState),
      For { _.zipCode } ForField 'zipCode
        must { _.length == 5 } withMessage "validation.address.invalidZipCode"
        must { _.forall(_.isDigit) } withMessage "validation.address.invalidZipCode"
    )
  }


}
