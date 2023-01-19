# Polyglot Chat Application

This example demonstrates how to integrate Python on GraalVM with a Java application.

### Prerequisites
- [Native Image](https://www.graalvm.org/latest/reference-manual/native-image/)
- [R support](https://www.graalvm.org/latest/reference-manual/r/)
- [Python support](https://www.graalvm.org/latest/reference-manual/python/)

## Preparation

1. Download and install the latest GraalVM JDK with Native Image, Python and R support using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) -c 'python,R'
    ```

## Building and Running the application:

1. Build application with Gradle:
    ```bash
    ./gradlew run
    ```

2. Navigate to http://localhost:8080

    You can connect from multiple browsers and chat via websockets. You can use "/"
    commands to trigger certain interesting functions. Available are:

        /img [some string]

    Searches for a random image from the internet matching [some string].

        /gif [some string]

    Searches for a GIF on the internet matching [some string].

        /= 1 2 3 4

    Echos the arguments `1 2 3 4`.

        /plot 1 1.4 2.6 6.4 25.6 102.4

    Plots the points given.

### Learn More 

Learn more about GraalVM polyglot capabilities [here](https://www.graalvm.org/latest/reference-manual/polyglot-programming/).