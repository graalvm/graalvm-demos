# Image Manipulation Using a Java Application with an Embedded Python Library

This small application demonstrates how to use GraalPy to create an instance of a proxied Python class from a Java application, built with Maven.

It includes the following steps:
1. Install the [pillow](https://pillow.readthedocs.io/en/stable/) and [requests](https://pypi.org/project/requests/) modules. (See the [pom.xml](./pom.xml) file for more details.)
2. Create a Python class ([pillowImageWrapper.py](src/main/resources/vfs/proj/pillowImageWrapper.py)) that creates, manipulates, and stores an image.
3. Create a Java interface ([PillowImageProxy.java](src/main/java/com/oracle/example/graalpy/PillowImageProxy.java)) that acts as a proxy for the wrapper.
4. Create a Swing JFrame ([PillowFrame.java](src/main/java/com/oracle/example/graalpy/PillowFrame.java)) that calls the proxy's methods. (This class also contains a `main()` method.)

## Preparation

Install a JDK, ensure that it is on your classpath, and set the value of `JAVA_HOME` accordingly.
We recommend GraalVM for JDK 21 that can be easily installed on macOS and Linux using [SDKMAN!](https://sdkman.io/). (For other download options, see [GraalVM Downloads](https://www.graalvm.org/downloads/).)
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

![Embedded Pillow Java Application](Embedded%20Pillow.gif)

See the [pom.xml](./pom.xml) file for configuration details.

> For more details on how to embed GraalPy in your Java applications, see [Embedding Languages](https://www.graalvm.org/latest/reference-manual/embed-languages/).