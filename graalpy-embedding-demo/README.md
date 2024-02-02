# GraalPy Embedding Demo JDK 21 
Demonstration application showing GraalPy embedding with JDK 21 using Maven. It contains a Main class that prints "Hello GraalPy!" when run. Shows how to produce a native executable using GraalVM.

For more details on polyglot embedding please see the docs:
https://www.graalvm.org/latest/reference-manual/embed-languages/

## Setup

Any JDK 21

## Usage

Download Maven or import as a Maven project into your IDE.

* build: `mvn package`
* run: `mvn exec:exec`

If you have [installed GraalVM](https://www.graalvm.org/downloads/):

* build a native executable: `mvn package -Pnative`
* run the native executable: `./target/embedded_graalpy`

Please see the [pom.xml](./pom.xml) file for further details on the configuration.