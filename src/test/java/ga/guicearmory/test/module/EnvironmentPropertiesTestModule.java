
package ga.guicearmory.test.module;

import ga.guicearmory.gini.annotations.BindEnvironmentProperties;
import ga.guicearmory.gini.modules.GiniModule;

@BindEnvironmentProperties({"NUMBER_OF_PROCESSORS"})
public class EnvironmentPropertiesTestModule extends GiniModule {
}
