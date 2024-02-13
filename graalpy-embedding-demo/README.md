# GraalPy Embedding Demo

This small application demonstrates how to embed GraalPy in a Java application using Maven. 
The application contains a `Main` class that evaluates a Python statement (to print "Hello GraalPy!").

The example also shows how to produce a native executable using [GraalVM](https://www.graalvm.org/).

## Preparation

Install a JDK 21, ensure that it is on your classpath, and set the value of `JAVA_HOME` accordingly.
We recommend GraalVM for JDK 21 that can be easily installed using [SDKMAN!](https://sdkman.io/).
```bash
sdk install java 21.0.2-graal
```

## Run the Application

1. Build the project:
    ```bash
    mvn package
    ```
2. Run the application:
    ```bash
    mvn exec:exec
    ```  

## (Optional) Build and Run a Native Executable 

If you [installed GraalVM](https://www.graalvm.org/downloads/) you can build this Java-Python application into a native executable and run it.

1. Build a native executable:
    ```bash
    mvn package -Pnative
    ```
    It creates the native executable called `embedded_graalpy` in the _target/_ directory.
2. Run the native executable:
    ```bash
    ./target/embedded_graalpy
    ```
  
See the [pom.xml](./pom.xml) file for configuration details.

> For more details on how to embed GraalPy in your Java applications, see [Embedding Languages](https://www.graalvm.org/latest/reference-manual/embed-languages/).