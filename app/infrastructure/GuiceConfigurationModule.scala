package infrastructure

import com.google.inject.AbstractModule

class GuiceConfigurationModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[StartUpConfigurator]).asEagerSingleton()
  }

}
