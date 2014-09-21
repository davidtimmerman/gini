package ga.guicearmory.gini.modules;

import com.google.inject.AbstractModule;
import ga.guicearmory.gini.annotations.*;
import ga.guicearmory.gini.util.PropertiesUtil;

import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

public class GiniModule extends AbstractModule {

    @Override
    protected void configure() {

        final Logger LOGGER = Logger.getLogger(GiniModule.class.getName());

        PropertiesUtil util = new PropertiesUtil();
        Properties properties = new Properties();
        Class gini  = this.getClass();

        if(gini.isAnnotationPresent(BindSystemProperties.class)){
            Annotation annotation = gini.getAnnotation(BindSystemProperties.class);
            BindSystemProperties bindSystemProperties = (BindSystemProperties) annotation;
            properties = util.getProperties(bindSystemProperties);
        }
        if(gini.isAnnotationPresent(BindEnvironmentProperties.class)){
            Annotation annotation = gini.getAnnotation(BindEnvironmentProperties.class);
            BindEnvironmentProperties bindEnvironmentProperties = (BindEnvironmentProperties) annotation;
            properties = util.getProperties(bindEnvironmentProperties);
        }
        if(gini.isAnnotationPresent(PropertySources.class)){
            PropertySources sources = (PropertySources) gini.getAnnotation(PropertySources.class);
            properties.putAll(util.getProperties(sources));
        }
        else if (gini.isAnnotationPresent(PropertySource.class)) {
            Annotation annotation = gini.getAnnotation(PropertySource.class);
            PropertySource source = (PropertySource) annotation;
            properties.putAll(util.getProperties(source));
        }
        Enumeration e = properties.propertyNames();

        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);

            if(value.startsWith("date:")){
                int start = value.indexOf('[')+1;
                int end = value.indexOf(']');
                String date = value.substring(end+1);
                String dateFormat = value.substring(start,end);
                SimpleDateFormat df = new SimpleDateFormat(dateFormat);
                try {
                    Date d = df.parse(date);
                    bind(Date.class)
                            .annotatedWith(new PropertyImpl(key))
                            .toInstance(d);
                } catch (ParseException e1) {
                    LOGGER.severe("key: "+key+" value: "+value+"; Parsing date threw an exception, property not bound");
                }
                ClassLoader classLoader = this.getClass().getClassLoader();
                try {
                    // is joda time on classpath?
                    classLoader.loadClass("org.joda.time.DateTime");

                    org.joda.time.DateTime dateTime  = org.joda.time.DateTime.parse(date, org.joda.time.format.DateTimeFormat.forPattern(dateFormat));
                    bind(org.joda.time.DateTime.class)
                            .annotatedWith(new PropertyImpl(key))
                            .toInstance(dateTime);
                } catch (ClassNotFoundException ex) {
                    LOGGER.info("Joda Time not found on classpath - not binding to DateTime");
                }

                bind(String.class)
                        .annotatedWith(new PropertyImpl(key))
                        .toInstance(date);

            }
            else if(value.startsWith("uri:")){
                //TODO implement this
            }
            else if(value.startsWith("file:")){
                //TODO implement this
            }
            else{
                bindConstant().annotatedWith(new PropertyImpl(key)).to(value);
            }

            LOGGER.info("Property value:"+ value + " is bound to key: "+key);
        }
    }
}
