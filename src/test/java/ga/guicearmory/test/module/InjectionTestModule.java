package ga.guicearmory.test.module;

import ga.guicearmory.gini.annotations.PropertySource;
import ga.guicearmory.gini.annotations.PropertySources;
import ga.guicearmory.gini.modules.GiniModule;
@PropertySources(propertySources = {
        //@PropertySource("file:src\\test\\resources\\file.test.properties"),
        @PropertySource("classpath:file.test.properties"),
        @PropertySource("classpath:classpath.test.properties")
})
public class InjectionTestModule extends GiniModule {
}
