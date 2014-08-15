package ga.guicearmory.test.module;

import ga.guicearmory.gini.annotations.BindSystemProperties;
import ga.guicearmory.gini.modules.GiniModule;

//@BindEnvironmentProperties({"NUMBER_OF_PROCESSORS"})
@BindSystemProperties({"java.runtime.name"})
public class SystemPropertiesTestModule extends GiniModule {
}
