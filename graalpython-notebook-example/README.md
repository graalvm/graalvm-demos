## Java and Python Integration Example for GraalVM

This example demonstrates how to integrate Python on GraalVM with a Java application.

## Prerequisites

- GraalVM
- Python support

### Getting Started

1. Download [GraalVM Commuity or Enterprise](https://www.graalvm.org/downloads/) and set your `JAVA_HOME` to point to it.

2. Add the Python support. GraalVM comes with `gu` which is a command line utility to install and manage additional functionalities, and to install Python, run this single command:
```bash
${JAVA_HOME}/gu install python
```

3. Compile the example:
```bash
mvn compile
```

4. Run the example:
```bash
mvn exec:java
```

In the application, try loading the _test1.py_ example notebook and experiment with it.
