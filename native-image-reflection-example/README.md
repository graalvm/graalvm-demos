# Native Image Support for Java Reflection
<!-- # Building a Native Executable with Java Reflection -->

[Reflection](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/package-summary.html) is a feature of the Java programming language that enables a running Java program to examine and modify attributes of its classes, interfaces, fields, and methods.

GraalVM Native Image provides partial support for reflection. It uses static analysis to detect the elements of your application that are accessed using the [Java Reflection API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/package-summary.html). However, because the analysis is static, it cannot always completely predict all usages of the API when the program runs. Undetected usages must be provided to the `native-image` tool in the form of metadata (precomputed in code or as JSON configuration files).

The following application demonstrates the use of Java reflection and how to provide metadata for `native-image` using a JSON configuration file.

## Preparation

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk)
    ```

2. Download or clone the repository and navigate into the `native-image-reflection-example` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-image-reflection-example
    ```

## Example with No Configuration

1. Compile the application and invoke the `StringReverser()` and `StringCapitalizer()` methods:
    ```shell
    $JAVA_HOME/bin/javac ReflectionExample.java
    ```
    ```shell
    $JAVA_HOME/bin/java ReflectionExample StringReverser reverse "hello"
    ```
    ```shell
    $JAVA_HOME/bin/java ReflectionExample StringCapitalizer capitalize "hello"
    ```

    The output of each command should be `"olleh"` and `"HELLO"`, respectively. (An exception is thrown if you provide any other string to identify the class or method.)
    
2. Use the `native-image` utility to create a native executable as follows:
    ```shell
    $JAVA_HOME/bin/native-image --no-fallback ReflectionExample
    ```
    > NOTE: The `--no-fallback` option to `native-image` causes the utility to fail if it can not create an executable file.

3. Run the resulting native executable, using the following command:
    ```bash
    ./reflectionexample StringReverser reverse "hello"
    ```
    You will see a `ClassNotFoundException` exception, similar to:
    ```shell
    Exception in thread "main" java.lang.ClassNotFoundException: StringReverser
        at java.lang.Class.forName(DynamicHub.java:1338)
        at java.lang.Class.forName(DynamicHub.java:1313)
        at ReflectionExample.main(ReflectionExample.java:25)
    ```

    This shows that, from its static analysis, the `native-image` tool was unable to determine that class `StringReverser` is used by the application and therefore did not include it in the native executable. 

## Example with Configuration

To build a native executable containing references to the classes and methods that are accessed via reflection, provide the `native-image` utility with a configuration file that specifies the classes and corresponding methods. (For more information about configuration files, see [Reflection Use in Native Images](https://www.graalvm.org/latest/reference-manual/native-image/dynamic-features/Reflection/). You can create this file by hand, but a more convenient approach is to generate it using the tracing agent. The agent writes the configuration for you automatically when you run your application (for more information, see [Assisted Configuration with Tracing Agent](https://www.graalvm.org/latest/reference-manual/native-image/metadata/AutomaticMetadataCollection/#tracing-agent)). 

The following steps demonstrate how to use the tracing agent tool, and its output, to create a native executable that relies on reflection.

1. Create a directory `META-INF/native-image` in the working directory:
    ```shell
    mkdir -p META-INF/native-image
    ```

2. Run the application with the tracing agent enabled, as follows:
    ```shell
    $JAVA_HOME/bin/java -agentlib:native-image-agent=config-output-dir=META-INF/native-image ReflectionExample StringReverser reverse "hello"
    ```
    This command creates a file named _reflection-config.json_ containing the name of the class `StringReverser` and its `reverse()` method.
    ```json
    [
        {
        "name":"StringReverser",
        "methods":[{"name":"reverse","parameterTypes":["java.lang.String"] }]
        }
    ]
    ```

3. Build a native executable:
    ```shell
    $JAVA_HOME/bin/native-image ReflectionExample
    ```
    
    The `native-image` tool automatically uses configuration files in the _META-INF/native-image_ directory.
    It is recommended that the _META-INF/native-image_ directory is on the class path, either via a JAR file or using the `-cp` flag. (This avoids confusion for IDE users where a directory structure is defined by the IDE itself.)

4. Test your executable:
    ```shell
    ./reflectionexample StringReverser reverse "hello"
    ```
    The output of command should be `"olleh"` 
   
    ```shell
    ./reflectionexample StringCapitalizer capitalize "hello"
    ```

    You will see a `ClassNotFoundException` exception again.

    Neither the tracing agent nor the `native-image` tool can ensure that the configuration file is complete.
    The agent observes and records which program elements are accessed using reflection when you run the program. In this case, the `native-image` tool has not been configured to include references to class `StringCapitalizer`.

5. Update the configuration to include class `StringCapitalizer`. You can manually edit the _reflection-config.json_ file or re-run the tracing agent to update the existing configuration file using the `config-merge-dir` option, as follows:
    ```shell
    $JAVA_HOME/bin/java -agentlib:native-image-agent=config-merge-dir=META-INF/native-image ReflectionExample StringCapitalizer capitalize "hello"
    ```

    This command updates the _reflection-config.json_ file to include the name of the class `StringCapitalizer` and its `capitalize()` method.
    ```json
    [
        {
        "name":"StringCapitalizer",
        "methods":[{"name":"capitalize","parameterTypes":["java.lang.String"] }]
        },
        {
        "name":"StringReverser",
        "methods":[{"name":"reverse","parameterTypes":["java.lang.String"] }]
        }
    ]
    ```

6. Rebuild a native executable and run the resulting executable:
    ```shell
    $JAVA_HOME/bin/native-image ReflectionExample
    ```
    ```shell
    ./reflectionexample StringCapitalizer capitalize "hello"
    ```
   
   The application should now work as intended.

### Related Documentation

* [Reachability Metadata: Reflection](https://www.graalvm.org/latest/reference-manual/native-image/metadata/)
* [Assisted Configuration with Tracing Agent](https://www.graalvm.org/latest/reference-manual/native-image/metadata/AutomaticMetadataCollection/#tracing-agent) 
* [java.lang.reflect Javadoc](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/package-summary.html)