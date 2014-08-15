package ga.guicearmory.test.integration;

import ga.guicearmory.gini.annotations.Property;
import ga.guicearmory.test.module.InjectionTestModule;
import ga.guicearmory.test.runner.GuiceJUnitRunner;
import ga.guicearmory.test.runner.GuiceModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ InjectionTestModule.class })
public class InjectionTest {

    @Inject @Property("test.key.1") int anInt;
    @Inject @Property("test.key.1") Integer boxed;
    @Inject @Property("test.key.1") long aLong;

    @Inject @Property("test.key.2") String aString;

    @Inject @Property("test.key.3") String gini;

    @Test
    public void injectPrimitiveTest(){
        assertEquals(3, anInt);
        assertEquals(3L, aLong);
    }

    @Test
    public void injectPrimitiveWithAutoboxingTest(){
        assertEquals(new Integer(3),boxed);
    }

    @Test
    public void injectStringTest(){
        assertEquals("three",aString);
        assertEquals("gini",gini);
    }
}
