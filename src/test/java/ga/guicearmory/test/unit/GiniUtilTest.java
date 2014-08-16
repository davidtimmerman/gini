package ga.guicearmory.test.unit;

import com.google.inject.Guice;
import ga.guicearmory.gini.annotations.PropertySource;
import ga.guicearmory.gini.util.PropertiesUtil;
import ga.guicearmory.test.module.BindingTestModule;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Annotation;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class GiniUtilTest {

    @Before
    public void before(){
        Guice.createInjector(new BindingTestModule());
    }

    @Test
    public void expressionTest(){

        String input = "${env}/test.properties";
        PropertiesUtil util = new PropertiesUtil();
        assertEquals("test/test.properties",util.resolveExpression(input));
    }
}
