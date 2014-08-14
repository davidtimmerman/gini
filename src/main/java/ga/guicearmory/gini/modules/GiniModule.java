package ga.guicearmory.gini.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ga.guicearmory.gini.util.PropertiesUtil;
import ga.guicearmory.gini.annotations.*;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

public class GiniModule extends AbstractModule {

    @Override
    protected void configure() {

        final Logger LOGGER = Logger.getLogger(GiniModule.class.getName());

        PropertiesUtil util = new PropertiesUtil();
        Properties properties = new Properties();
        Class obj  = this.getClass();

        if(obj.isAnnotationPresent(BindSystemProperties.class)){
            Annotation annotation = obj.getAnnotation(BindSystemProperties.class);
            BindSystemProperties bindSystemProperties = (BindSystemProperties) annotation;
            properties = util.getProperties(bindSystemProperties);
        }
        if(obj.isAnnotationPresent(BindEnvironmentProperties.class)){
            Annotation annotation = obj.getAnnotation(BindEnvironmentProperties.class);
            BindEnvironmentProperties bindEnvironmentProperties = (BindEnvironmentProperties) annotation;
            //Set<String> envPropKeys = System.getenv().keySet();
 /*           for(String key : envPropKeys){
                if(properties.containsKey(key)){
                    LOGGER.warning("System property with key: "+key+ " is overwritten by Environment property with same key" );
                }
            }*/
            properties = util.getProperties(bindEnvironmentProperties);
        }
        if(obj.isAnnotationPresent(PropertySources.class)){
            PropertySources sources = (PropertySources) obj.getAnnotation(PropertySources.class);
            properties.putAll(util.getProperties(sources));
        }
        else if (obj.isAnnotationPresent(PropertySource.class)) {
            Annotation annotation = obj.getAnnotation(PropertySource.class);
            PropertySource source = (PropertySource) annotation;
            properties.putAll(util.getProperties(source));
        }
        Enumeration e = properties.propertyNames();

        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);

            bindConstant().annotatedWith(new PropertyImpl(key)).to(value);
            LOGGER.info("Property "+ value + " is bound to key: "+key);



        }


        }

    @Provides
    URI provideURI(String s){
        try {
            if(s==null){
                s="http://java.sun.com/j2se/1.3/";
            }
            return new URI(s);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
