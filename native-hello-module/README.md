# Build a Java Module into a Native Executable

GraalVM Native Image supports the Java Platform Module System, introduced in Java 9, which means you can convert a modularized Java application into a native executable.

The `native-image` tool accepts the module-related arguments like `--module (-m)`, `--module-path (-p)`, `--add-opens`, `--add-exports` (same as for the java launcher). When such a module-related argument is used, the `native-image` tool itself is used as a module too.

In addition to supporting `--add-reads` and `--add-modules`, all module related options are considered prior to scanning the modulepath. This helps prevent class loading errors and allow for better module introspection at run time.

The command to build a native executable from a Java module is:
```bush
native-image [options] --module <module>[/<mainclass>] [options]
```
The example below shows how to build a modular Java application into a native executable. 

### Prerequisites

- [GraalVM](http://graalvm.org)
- [Native Image](https://www.graalvm.org/latest/reference-manual/native-image/)

## Preparation

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) -c 'native-image'
    ```

2. Download or clone the repository and navigate into the `native-hello-module` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-hello-module
    ```

## Run the Application

For the demo, you will use a simple _HelloWorld_ Java module gathered with Maven:

    ├── hello
    │   └── Main.java
    │       > package hello;
    │       > 
    │       > public class Main {
    │       >     public static void main(String[] args) {
    │       >         System.out.println("Hello from Java Module: "
    │       >             + Main.class.getModule().getName());
    │       >     }
    │       > }
    │
    └── module-info.java
        > module HelloModule {
        >     exports hello;
        > }

1. Compile and package the project with Maven:
    ```bash
    $JAVA_HOME/bin/mvn package
    ```
    This creates a JAR file with all dependencies embedded: `target/HelloModule-1.0-SNAPSHOT.jar`

2. Test running it on GraalVM’s JDK:
    ```bash    
    $JAVA_HOME/bin/java --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule
    ```
    You should see the next  output:
    ```
    Hello from Java Module: HelloModule
    ```

3. Build this module into a native executable:
    ```bash 
    $JAVA_HOME/bin/native-image --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule
    ```                                 
    It builds the modular Java application into a native executable called `hellomodule` in the project root. Run it:
    ```bash
    ./hellomodule 
    ```
    You should see the same output:
    ```
    Hello from Java Module: HelloModule
    ```
### Related Documentation

- Learn more how you can access [resources for a Java module at run time](https://www.graalvm.org/latest/reference-manual/native-image/dynamic-features/Resources/#resources-in-java-modules).
