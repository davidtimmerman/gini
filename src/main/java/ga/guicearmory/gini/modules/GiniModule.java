package ga.guicearmory.gini.modules;

import com.google.inject.AbstractModule;
import com.sun.jndi.toolkit.url.Uri;
import ga.guicearmory.gini.annotations.*;
import ga.guicearmory.gini.util.Gini;
import ga.guicearmory.gini.util.PropertiesUtil;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
                    /*
                    load joda classes via reflection
                    if joda is not on the classpath this silently fails
                     */
                    Class DateTime = classLoader.loadClass("org.joda.time.DateTime");
                    Class DateTimeFormat = classLoader.loadClass("org.joda.time.format.DateTimeFormat");
                    Class DateTimeFormatter = classLoader.loadClass("org.joda.time.format.DateTimeFormatter");
                    try {
                        Method parse = DateTime.getDeclaredMethod("parse",String.class,DateTimeFormatter);
                        Method forPattern = DateTimeFormat.getMethod("forPattern",String.class);
                        Object o  = parse.invoke(null, new Object[] {date, DateTimeFormatter.cast(forPattern.invoke(null, new Object[] {dateFormat}))});
                        bind(DateTime)
                                .annotatedWith(new PropertyImpl(key))
                                .toInstance(DateTime.cast(o));
                    } catch (NoSuchMethodException e1) {
                        e1.printStackTrace();
                    } catch (InvocationTargetException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }


                } catch (ClassNotFoundException ex) {
                    LOGGER.info("Joda Time not found on classpath - not binding to DateTime");
                }

                bind(String.class)
                        .annotatedWith(new PropertyImpl(key))
                        .toInstance(date);

            }
            else if(value.startsWith("uri:")){
                int end = value.indexOf(':');
                String val = value.substring(end+1);

                try {
                    URI uri = new URI(val);

                    bind(URI.class)
                            .annotatedWith(new PropertyImpl(key))
                            .toInstance(uri);
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }

            }
            else if(value.startsWith("dir:")){

                int end = value.indexOf(':');
                String path = value.substring(end+1);

                File file = new File(path);
                if(!file.exists()) {
                    file.mkdirs();
                }

                bind(File.class).annotatedWith(new PropertyImpl(key)).toInstance(file);
            }
            else{
                bindConstant().annotatedWith(new PropertyImpl(key)).to(value);
            }

            LOGGER.info("Property value:"+ value + " is bound to key: "+key);
        }

        requestStaticInjection(Gini.class);
    }
}
