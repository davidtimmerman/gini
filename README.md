Gini
====

Lightweight extension for Guice to conveniently inject properties.

- **Documentation**: [Wiki] (https://github.com/davidtimmerman/gini/wiki)
- **License**: [Apache v 2.0] (http://www.apache.org/licenses/LICENSE-2.0)

#Quick start

Create a class that extends from GiniModule and add @PropertySource passing your properties file as a string
```java
@PropertySource("classpath:my.properties")
public class MyModule extends GiniModule {
}
```

Add it where you create a new Guice injector

```java
Injector injector = Guice.createInjector(new MyModule());
```

Inject values from your properties file with field injection
```java
@Inject @Property("aKey") String aValue;
```
or method injection

```java
    @Inject
    public MyClass(@Property("aKey") aValue) {
        this.aValue = aValue;
    }
```

[![endorse](https://api.coderwall.com/davidtimmerman/endorsecount.png)](https://coderwall.com/davidtimmerman)