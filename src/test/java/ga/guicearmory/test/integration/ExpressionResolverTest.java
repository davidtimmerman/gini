package ga.guicearmory.test.integration;

import ga.guicearmory.gini.annotations.Property;
import ga.guicearmory.test.module.ExpressionTestModule;
import ga.guicearmory.test.module.InjectionTestModule;
import ga.guicearmory.test.runner.GuiceJUnitRunner;
import ga.guicearmory.test.runner.GuiceModules;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ExpressionTestModule.class })
public class ExpressionResolverTest {

    @Inject @Property("test.key.4") String expr;

    @Test
    @Ignore
    public void injectPropertyFromFileWithExpressionTest(){
        assertEquals("expression",expr);
    }
}
