package ga.guicearmory.test.integration;

import com.google.inject.Inject;
import ga.guicearmory.gini.annotations.Property;
import ga.guicearmory.test.runner.GuiceJUnitRunner;
import ga.guicearmory.test.runner.GuiceModules;
import ga.guicearmory.test.module.EnvironmentPropertiesTestModule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ EnvironmentPropertiesTestModule.class })
public class EnvironmentPropertiesTest {

    @Inject @Property("NUMBER_OF_PROCESSORS") String nrOfProcessors;

    @Test
    public void bindEnvironmentPropertyTest(){

        assertNotNull(nrOfProcessors);
    }
}
