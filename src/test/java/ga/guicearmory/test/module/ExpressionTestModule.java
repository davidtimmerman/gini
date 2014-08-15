package ga.guicearmory.test.module;

import ga.guicearmory.gini.annotations.PropertySource;
import ga.guicearmory.gini.modules.GiniModule;

@PropertySource("classpath:${env}/expression.test.properties")
public class ExpressionTestModule extends GiniModule {
}
