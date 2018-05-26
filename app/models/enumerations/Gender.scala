package models.enumerations

import com.github.novamage.svalidator.utils.TypeBasedEnumeration

sealed abstract case class Gender(id: Int, description: String) extends Gender.Value

object Gender extends TypeBasedEnumeration[Gender] {

  object Male extends Gender(1,"enumeration.gender.male")

  object Female extends Gender(2,"enumeration.gender.female")

}
