# GraalPy Evaluating Demo JDK 21 
Demonstration application showing how to embed GraalPy in a Java application using Maven. 
It contains a Main class that evaluates an external Python file named _Hello.py_ when run. 
It also shows how to produce a native executable using GraalVM.

For more details on polyglot embedding please see the docs:
https://www.graalvm.org/latest/reference-manual/embed-languages/

## Setup

Any JDK 21

## Usage

1. Download Maven or import as a Maven project into your IDE.

    * build: `mvn package`
    * run: `mvn exec:exec`

2. To confirm that your application reads the contents of _Hello.py_, edit the file, as follows:
    ```python
    print ("Hello Evaluated GraalPy!")
    ```
    then run the application again (using `mvn exec:exec`).


## Create a Native Executable

1. If you have [installed GraalVM](https://www.graalvm.org/downloads/):

    * build a native executable: `mvn package -Pnative`
    * run the native executable: `./target/eval_graalpy`

2. To confirm that your application reads the contents of _Hello.py_, edit the file, as follows:
    ```python
    print ("Hello Native Evaluated GraalPy!")
    ```
    then run the application again (using `./target/eval_graalpy`).

Please see the [pom.xml](./pom.xml) file for further details on the configuration.