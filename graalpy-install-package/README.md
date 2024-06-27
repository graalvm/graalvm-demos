# Using GraalPy to Access a Python Class from a Java Application 

This small application demonstrates how to use GraalPy to access a Python class from a Java application, built with Maven.

It includes the following steps:
1. Install the [Pyfiglet](https://github.com/pwaller/pyfiglet) module. (Pyfiglet is an ASCII art module to create styled text.)
2. Create a Python class ([pyfigletwrapper.py](src/main/resources/vfs/proj/pyfigletwrapper.py)) that acts as a wrapper for the pyfiglet module.
3. Create a Java interface ([PyfigletProxy.java](src/main/java/com/oracle/example/graalpy/PyfigletProxy.java)) that acts as a proxy for the wrapper.
4. Create a Swing JFrame ([PyfigletFrame.java](src/main/java/com/oracle/example/graalpy/PyfigletFrame.java)) that calls the proxy's `format` method.
5. Create a [Main](src/main/java/com/oracle/example/graalpy/Main.java) class that sets up the GraalPy environment, creates an instance of `PyfigletProxy`, then creates and opens an instance of `PyfigletFrame`.

## Preparation

Install a JDK 21, ensure that it is on your classpath, and set the value of `JAVA_HOME` accordingly.
We recommend GraalVM for JDK 21 that can be easily installed using [SDKMAN!](https://sdkman.io/). (For other download options, see [GraalVM Downloads](https://www.graalvm.org/downloads/).)
```bash
sdk install java 21.0.2-graal
```

## Run the Application

1. Build the application:
    ```bash
    mvn package
    ```
2. Run the application:
    ```bash
    mvn exec:exec
    ```  


![Pyfiglet Java Application](Pyfiglet%20GUI.gif)

## (Optional) Build and Run a Native Executable

If you [installed GraalVM](https://www.graalvm.org/downloads/), you can use GraalVM Native Image to build this Java-Python application into a native executable and then run it.

> Note: The Native Image build configuration files are in [src/main/resources/META-INF/native-image](src/main/resources/META-INF/native-image). For more information, see [Native Image Build Configuration](https://www.graalvm.org/latest/reference-manual/native-image/overview/BuildConfiguration/).

1. Build a native executable:
    ```bash
    mvn package -Pnative
    ```
    It creates the native executable called `package-graalpy` in the _target/_ directory.

2. Run the native executable:
    ```bash
    ./target/package-graalpy
    ```

See the [pom.xml](./pom.xml) file for configuration details.

> For more details on how to embed GraalPy in your Java applications, see [Embedding Languages](https://www.graalvm.org/latest/reference-manual/embed-languages/).
