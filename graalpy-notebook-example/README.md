# Java and Python Integration Example for GraalVM

This example demonstrates how to integrate Python in a Java application and run on GraalVM.

### Prerequisites

- GraalVM
- [Python support](https://www.graalvm.org/latest/reference-manual/python/)

## Preparation

1. Download and install the latest GraalVM JDK with the Python support using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader). 
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) -c 'python'
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
