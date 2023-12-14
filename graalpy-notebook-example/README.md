# Java and Python Integration Example

This example demonstrates how to integrate Python in a Java application and run on GraalVM.

## Build and Run Demo

1. Compile the example:
    ```bash
    mvn compile
    ```

2. Run the example:
    ```bash
    mvn exec:java -Dexec.mainClass=com.oracle.example.javapython.Main
    ```

In the application, try loading the _test1.py_ example notebook and experiment with it.

This demo works on Open JDK, Oracle JDK or GraalVM.
GraalVM offers (much) better performance than the other two.
