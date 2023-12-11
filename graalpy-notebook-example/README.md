# Java and Python Integration Example for GraalVM

This example demonstrates how to integrate Python in a Java application and run on GraalVM.

### Prerequisites

- [GraalVM](https://www.graalvm.org/)
- [GraalPy](https://www.graalvm.org/latest/reference-manual/python/)

>Note: As of GraalVM for JDK 21, the Python runtime (GraalPy) is available as a standalone distribution.

## Preparation

1. Download and install the latest GraalVM JDK using [SDKMAN!](https://sdkman.io/).
    ```bash
    sdk install java 21.0.1-graal
    ```
2. Download and install  GraalPy using `pyenv`:
    ```bash
    pyenv install graalpy-23.1.0
    ```
    Alternatively, download a compressed GraalPy installation file appropriate for your platform from [GitHub releases](https://github.com/oracle/graalpython/releases).

3. Set the `GRAALPY_HOME` environment variable:
    ```bash
    export GRAALPY_HOME=/.pyenv/versions/graalpy-23.1.0
    ```
2. Download or clone GraalVM demos repository and navigate into the `graalpy-notebook-example` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/graalpy-notebook-example
    ```

## Build and Run Demo

1. Compile the example:
    ```bash
    mvn compile
    ```

2. Install the dependencies:
    ```bash
    ./install-deps.sh
    ```

3. Run the example:
    ```bash
    mvn exec:java
    ```

In the application, try loading the _test1.py_ example notebook and experiment with it.
