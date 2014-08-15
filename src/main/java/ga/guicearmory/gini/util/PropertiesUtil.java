package ga.guicearmory.gini.util;

import ga.guicearmory.gini.annotations.BindEnvironmentProperties;
import ga.guicearmory.gini.annotations.BindSystemProperties;
import ga.guicearmory.gini.annotations.PropertySource;
import ga.guicearmory.gini.annotations.PropertySources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesUtil {

    final Logger LOGGER = Logger.getLogger(PropertiesUtil.class.getName());

    public Properties getProperties(PropertySource source) {
        String file = resolveExpression(source.value());
        StringBuilder b = new StringBuilder(file);
        if (file.startsWith("file:")) {
            String s = file.replaceFirst("file:","");
            return readFromFileSystem(s);
        }
        else if (file.startsWith("classpath:")){
            String s = file.replaceFirst("classpath:","");
            return readFromClasspath(s);
        }
        else return readFromClasspath(file);
    }

    public Properties getProperties(PropertySources sources){
        Properties properties = new Properties();
        for(PropertySource source : sources.propertySources()){
            properties.putAll(getProperties(source));
        }
        return properties;
    }
    public Properties getProperties(BindSystemProperties systemProperties){
        Properties properties = new Properties();
        if(systemProperties.value()[0].equals(BindSystemProperties.ALL)){
            properties.putAll(System.getProperties());
        }
        else{
            for(String key : systemProperties.value()){
                String value = System.getProperty(key);
                if(value!=null){
                    properties.put(key,value);
                }
                else{
                    LOGGER.severe("Property with key: "+key+ " was not found in system properties. Property was skipped!");
                }
            }
        }
        return properties;
    }

    public Properties getProperties(BindEnvironmentProperties environmentProperties){
        Properties properties = new Properties();
        if(environmentProperties.value()[0].equals(BindEnvironmentProperties.ALL)){
            properties.putAll(System.getenv());
        }
        else{
            for(String key : environmentProperties.value()){

                String value = System.getenv(key);
                if(value!=null){
                    properties.put(key,value);
                }
                else{
                    LOGGER.severe("Property with key: "+key+ " was not found in environment properties. Property was skipped!");
                }
            }
        }
        return properties;
    }

    public Properties readFromClasspath(String fileName) {
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;

    }
    public Properties readFromFileSystem(String filePath){
        Properties properties = new Properties();
        try {
            InputStream input = new FileInputStream(filePath);
            properties.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public String resolveExpression(String expression){

        Pattern pattern = Pattern.compile("\\$\\{\\w+\\}");
        Matcher matcher = pattern.matcher(expression);

        if(!matcher.find()){
            return expression;
        }
        matcher.reset();
        StringBuilder result = new StringBuilder(expression);

        LinkedList<Replacement> rep = new LinkedList<Replacement>();

        while(matcher.find()){
            String key = expression.substring(matcher.start()+2,matcher.end()-1);
            String value = System.getenv(key);
            if(value.equals(null)){
                throw new RuntimeException("Guice iNi was unable to load "+key+ " as environment variable. It does not exist");
            }
            rep.addFirst(new Replacement(matcher.start(),matcher.end(),value));
        }
        for (Replacement r : rep) {
            result.replace(r.start,r.end,r.value);
        }
        return result.toString();
    }

    private class Replacement{
        int start;
        int end;
        String value;

        private Replacement(int start, int end, String value) {
            this.start = start;
            this.end = end;
            this.value = value;
        }
    }

}
