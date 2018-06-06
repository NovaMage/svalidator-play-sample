package infrastructure

import com.github.novamage.svalidator.binding.{BindingConfig, TypeBinderRegistry}
import models.domain.Address

class StartUpConfigurator {

  TypeBinderRegistry.initializeBinders(new BindingConfig(dateFormat = StartUpConfigurator.dateFormat, languageConfig = ApplicationBindingLanguageConfig))
  TypeBinderRegistry.allowRecursiveBindingForType[Address]()

}

object StartUpConfigurator {

  val dateFormat = "dd/MM/yyyy"
}
