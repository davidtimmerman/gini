package ga.guicearmory.gini.util;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import ga.guicearmory.gini.annotations.PropertyImpl;

public class Gini {

    @Inject
    private static Injector injector;

    private Gini(){
    }

    public static <T> T getProperty(Class<T> clazz, String key){
        return injector.getInstance(Key.get(clazz, new PropertyImpl(key)));
    }
}
