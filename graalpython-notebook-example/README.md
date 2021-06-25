## Java and Python Integration Example for GraalVM

This example demonstrates how to integrate Python on GraalVM with a Java application.

### Getting Started

1. Download [GraalVM CE or EE](https://www.graalvm.org/downloads/) and set your `JAVA_HOME` to point to it. Make sure you have installed Python support:
```
"${JAVA_HOME}"/gu install python
```

2. Compile the example:
```
mvn compile
```

3. Run the example:
```
mvn exec:java
```

In the application, try loading the test1.py example notebook and experiment with it.

