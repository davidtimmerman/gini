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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class GiniUtilTest {

    @Before
    public void before(){
        Guice.createInjector(new BindingTestModule());
    }

    @Test
    public void tester(){
        Properties props = new Properties();
        props.put("env","dev");
        props.put("filename","guicearmory");

        new Expectations()
        {
            @Mocked("getenv") System mockedSystem;

            {
                System.getenv("env"); returns("dev");
                System.getenv("filename"); returns("guicearmory");

            }
        };

        String input = "${env}/${filename}.properties";
        PropertiesUtil h = new PropertiesUtil();
        System.out.println(h.resolveExpression(input));
        assertTrue(true);
    }




    @Test
    public void resolvePropertySourceTest(){
        PropertiesUtil helper = new PropertiesUtil();
        PropertySource propertySource = new PropertySource(){
            @Override
            public String value() {
                return "file:src/main/resources/dev/guicearmory.properties";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };
        assertNotNull(helper.getProperties(propertySource));

    }

}
