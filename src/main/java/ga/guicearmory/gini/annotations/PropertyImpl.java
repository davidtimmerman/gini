package ga.guicearmory.gini.annotations;

import java.lang.annotation.Annotation;

public class PropertyImpl implements Property {

    private final String value;

    public PropertyImpl(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public int hashCode() {
        // This is specified in java.lang.Annotation.
        return (127 * "value".hashCode()) ^ value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Property)) {
            return false;
        }

        Property other = (Property) o;
        return value.equals(other.value());
    }

    @Override
    public String toString() {
        return "@" + Property.class.getName() + "(value=" + value + ")";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Property.class;
    }
}