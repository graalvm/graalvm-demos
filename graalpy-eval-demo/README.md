# GraalPy Evaluating Demo
This small application demonstrates how to use GraalPy to evaluate a Python file from a Java application, built with Maven.
The application contains a Main class that evaluates an external Python file named _Hello.py_ when run.

The example also shows how to produce a native executable using [GraalVM](https://www.graalvm.org/).

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

3. To confirm that your application reads the contents of _Hello.py_, edit the file, as follows:
    ```python
    print ("Hello Evaluated GraalPy!")
    ```
    Then, run the application again (using `mvn exec:exec`).


## (Optional) Build and Run a Native Executable

If you [installed GraalVM](https://www.graalvm.org/downloads/), you can build this Java-Python application into a native executable and run it.

1. Build a native executable:
    ```bash
    mvn package -Pnative
    ```
    It creates the native executable called `eval_graalpy` in the _target/_ directory.

2. Run the native executable:
    ```bash
    ./target/eval_graalpy
    ```

3. To confirm that your application reads the contents of _Hello.py_, edit the file, as follows:
    ```python
    print ("Hello Native Evaluated GraalPy!")
    ```
    Then, run the application again (using `./target/eval_graalpy`).

See the [pom.xml](./pom.xml) file for configuration details.

> For more details on how to embed GraalPy in your Java applications, see [Embedding Languages](https://www.graalvm.org/latest/reference-manual/embed-languages/).