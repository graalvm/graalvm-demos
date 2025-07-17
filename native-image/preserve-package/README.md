# Using Native Image `Preserve` Option

[Reflection](https://docs.oracle.com/en/java/javase/24/docs/api/java.base/java/lang/reflect/package-summary.html) is a feature of the Java programming language that enables a running Java program to examine and modify attributes of its classes, interfaces, fields, and methods and GraalVM Native Image provides automatic support for some uses. Native Image uses static analysis to identify what classes, methods, and fields are needed by an application but it may not detect some elements of your application that are accessed using the [Java Reflection API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/package-summary.html). Undetected Reflection usage must be declared to the `native-image` tool either in the form of metadata (precomputed in code or as JSON configuration files) or using the `-H:Preserve` option (experimental in GraalVM for JDK 25).

The following demonstrates how to declare Reflection configuration using the `-H:Preserve` option.

## Preparation

1. Download and install the latest GraalVM for JDK 25 (or early access build before 2025-09-16) using [SDKMAN!](https://sdkman.io/).

    ```shell
    sdk install java 25.ea.29-graal
    ```

2. Download or clone this repository and navigate into the `native-image/preserve-package` directory:

    ```shell
    git clone https://github.com/graalvm/graalvm-demos
    ```

    ```shell
    cd graalvm-demos/native-image/preserve-package
    ```

## Example using Reflection on the JVM

The `ReflectionExample` class will use command line argument values to
reflectively create an instance of a class and invoke a method with a
provided argument. The core code is:

```java
    Class<?> clazz = Class.forName(className);
    Method method = clazz.getDeclaredMethod(methodName, String.class);
    Object result = method.invoke(null, input);
```

This works fine when running the JVM and the named classes and methods are on
the application classpath.

1. Compile the application and create a jar using Maven:
    ```shell
    ./mvnw package 
    ```
2. Run `ReflectionExample` (the jar entry point) and instruct it to invoke the
   `StringReverser` action:

    ```shell
    $JAVA_HOME/bin/java -jar target/preserve-package-1.0-SNAPSHOT.jar \
       org.graalvm.example.action.StringReverser reverse "hello"
    ```

    Expected output:
    ```shell
    olleh
    ```

3. Do the same for the `StringCapitalizer` action:
    ```shell
    $JAVA_HOME/bin/java -jar target/preserve-package-1.0-SNAPSHOT.jar \
       org.graalvm.example.action.StringCapitalizer capitalize "hello"
    ```
    Expected output:
    ```shell
    HELLO
    ```

## GraalVM Native Image

We can compile with Native Image specifying the `ReflectionExample` as the main
entry point.  The project [`pom.xml`](pom.xml) uses the [GraalVM Native Build
Tools](https://graalvm.github.io/native-build-tools/latest/index.html) plugin to
compile the project using the `native-image` tool when the `native-default`
profile is specified.

1. Build a native executable using the `native-default` profile (see [`pom.xml`](pom.xml)):
    ```shell
    ./mvnw package -Pnative-default  
    ```
4. Run the resulting `example-default` native executable, using the following command:
    ```bash
    ./target/example-default \
       org.graalvm.example.action.StringReverser reverse "hello"
    ```
    You will see a `ClassNotFoundException` exception, similar to:
    ```shell
    Exception in thread "main" java.lang.ClassNotFoundException: org.graalvm.example.action.StringReverser
        at org.graalvm.nativeimage.builder/com.oracle.svm.core.hub.ClassForNameSupport.forName(ClassForNameSupport.java:339)
        at org.graalvm.nativeimage.builder/com.oracle.svm.core.hub.ClassForNameSupport.forName(ClassForNameSupport.java:298)
        at java.base@25/java.lang.Class.forName(DynamicHub.java:1758)
        at java.base@25/java.lang.Class.forName(DynamicHub.java:1704)
        at java.base@25/java.lang.Class.forName(DynamicHub.java:1691)
        at org.graalvm.example.ReflectionExample.main(ReflectionExample.java:56)
        at java.base@25/java.lang.invoke.LambdaForm$DMH/sa346b79c.invokeStaticInit(LambdaForm$DMH)
    ```
What happened!? Based on its static analysis, the `native-image` tool was unable
to determine that class `StringReverser` is used by the application and
therefore did not include it in the native executable.

## Native Image using -H:Preserve

New in GraalVM for JDK 25 is the `-H:Preserve` option which makes it easy to instruct the
`native-image` tool to preserve (i.e., keep entirely) packages, modules, and
even all classes on the classpath (which can result in very large applications).  

Conveniently in this example, both of the classes that are being used via
reflection are in the `org.graalvm.example.action` package. We can use
`-H:Preserve=package` to keep all of the classes in that package in the native
executable, even though their use is not discoverable through static analysis.

Native Image command line arguments can be specified as `<buildArgs>` in the
`native-maven-plugin` configuration.  Note that since the `-H:Preserve` option
is new and considered experimental in GraalVM for JDK 25, you must also enable
its use with `-H:+UnlockExperimentalVMOptions`. See the [`pom.xml`](pom.xml) for
the complete plugin configuration:

```xml
    <configuration>
    ...
        <buildArgs>
            <buildArg>-H:+UnlockExperimentalVMOptions</buildArg>
            <buildArg>-H:Preserve=package=org.graalvm.example.action</buildArg>
        </buildArgs>
    </configuration>
```

1. Build a native executable using the `native-preserve` profile which adds
`-H:Preserve=package=org.graalvm.example.action` when running the `native-image`
   tool (see [`pom.xml`](pom.xml)):
    ```shell
    ./mvnw package -Pnative-preserve   
    ```

2. Run the new `example-preserve` executable to confirm the previously missing
   `StringReverser` class and all its methods are now included:
    ```shell
    ./target/example-preserve \
       org.graalvm.example.action.StringReverser reverse "hello"
    ```
    The expected "olleh" output should be the result, just like on the JVM.

3. Invoke the `StringCapitalizer` to see if that now works too:
   
    ```shell
    ./target/example-preserve \
       org.graalvm.example.action.StringCapitalizer capitalize "hello"
    ```
    The expected "HELLO" output should be the result.
 
   
As demonstrated, `-H:Preserve` provides an easy way to ensure classes not
discovered by GraalVM Native Image's static analysis are included in an
executable.

### Related Documentation

* [Reachability Metadata: Reflection](https://www.graalvm.org/latest/reference-manual/native-image/metadata/)
* [Assisted Configuration with Tracing Agent](https://www.graalvm.org/latest/reference-manual/native-image/metadata/AutomaticMetadataCollection/#tracing-agent) 
* [java.lang.reflect Javadoc](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/package-summary.html)