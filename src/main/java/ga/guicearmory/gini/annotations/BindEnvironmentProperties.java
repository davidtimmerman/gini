package ga.guicearmory.gini.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindEnvironmentProperties {
    final String ALL = "all";
    String[] value() default ALL;
}
