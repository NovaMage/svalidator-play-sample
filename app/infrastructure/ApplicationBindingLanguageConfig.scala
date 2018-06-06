package infrastructure

import com.github.novamage.svalidator.binding.BindingLanguageConfig
import com.github.novamage.svalidator.validation.MessageParts

object ApplicationBindingLanguageConfig extends BindingLanguageConfig {

  override def noValueProvidedMessage(fieldName: String): MessageParts = MessageParts("binding.required")

  override def invalidNonEmptyTextMessage(fieldName: String): MessageParts = MessageParts("binding.required")

  override def invalidBooleanMessage(fieldName: String, fieldValue: String): MessageParts = MessageParts("binding.invalidBoolean", List(fieldValue))

  override def invalidIntegerMessage(fieldName: String, fieldValue: String): MessageParts = MessageParts("binding.invalidInteger", List(fieldValue))

  override def invalidLongMessage(fieldName: String, fieldValue: String): MessageParts = MessageParts("binding.invalidInteger", List(fieldValue))

  override def invalidFloatMessage(fieldName: String, fieldValue: String): MessageParts = MessageParts("binding.invalidDecimal", List(fieldValue))

  override def invalidDoubleMessage(fieldName: String, fieldValue: String): MessageParts = MessageParts("binding.invalidDecimal", List(fieldValue))

  override def invalidDecimalMessage(fieldName: String, fieldValue: String): MessageParts = MessageParts("binding.invalidDecimal", List(fieldValue))

  override def invalidTimestampMessage(fieldName: String, fieldValue: String): MessageParts = MessageParts("binding.invalidDate", List(StartUpConfigurator.dateFormat))

  override def invalidEnumerationMessage(fieldName: String, fieldValue: String): MessageParts = MessageParts("binding.invalidEnumeration", List(fieldValue))
}
