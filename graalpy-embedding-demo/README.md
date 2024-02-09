# GraalPy Embedding Demo
This small application demonstrates how to embed GraalPy in a Java application using Maven. 
The application contains a `Main` class that evaluates a Python statement (to print "Hello GraalPy!").

The example also shows how to produce a native executable using [GraalVM](https://www.graalvm.org/).

>For more details on how to embed GraalPy in your Java applications, see [Embedding Languages](https://www.graalvm.org/latest/reference-manual/embed-languages/).

## Setup

You have installed a JDK 21, ensured that it is on your classpath, and set the value of `JAVA_HOME` accordingly.

## Usage
* build: `mvn package`
* run: `mvn exec:exec`

If you have [installed GraalVM](https://www.graalvm.org/downloads/):

* build a native executable: `mvn package -Pnative`
* run the native executable: `./target/embedded_graalpy`

Please see the [pom.xml](./pom.xml) file for configuration details.