package ga.guicearmory.test.integration;

import com.google.inject.Inject;
import ga.guicearmory.gini.annotations.Property;
import ga.guicearmory.test.runner.GuiceJUnitRunner;
import ga.guicearmory.test.runner.GuiceModules;
import ga.guicearmory.test.module.SystemPropertiesTestModule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ SystemPropertiesTestModule.class })
public class SystemPropertiesTest {

    @Inject @Property("java.runtime.name") String runtime;

    @Test
    public void bindSystemPropertyTest(){

        assertNotNull(runtime);
    }
}

