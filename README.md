gini
====

Lightweight framework built on Guice for conviently injecting properties

#Quick start

Create a class that extends from GiniModule
```java
@PropertySource("classpath:my.properties")
public class MyModule extends GiniModule {
}
```

Add it where you create a new Guice injector

    Injector injector = Guice.createInjector(new MyModule());

Inject values from your properties file with field injection

    @Inject @Property("aKey") String aValue;

or method injection

    @Inject
    public MyClass(@Property("aKey") aValue) {
        this.aValue = aValue;
    }

#Binding system properties

#Binding environment properties

