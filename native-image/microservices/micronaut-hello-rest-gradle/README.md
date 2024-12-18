# Micronaut REST Application Using Gradle and GraalVM Native Image

This example shows how to run a simple [Micronaut](https://micronaut.io/) REST application, compile it with [GraalVM Native Image](https://www.graalvm.org/reference-manual/native-image/), and package it in a container image.
Along the way you will see the performance benefits that GraalVM Native Image provides to Micronaut applications.

## Preparation

1. Download and install GraalVM using [SDKMAN!](https://sdkman.io/):
    ```bash
    sdk install java 21.0.5-graal
    ```
    > Note: A Java version between 17 and 21 is required to execute Gradle (see the Gradle Compatibility Matrix).

2. Download or clone GraalVM demos repository and navigate into the example directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-image/microservices/micronaut-hello-rest-gradle
    ```

## Compile and Run from a JAR File

1. Compile the application and build a fat JAR that includes all its dependencies:
    ```bash
    ./gradlew assemble
    ```

2. Run the application on HotSpot from a JAR file:
    ```bash
    java -jar build/libs/hello-0.1-all.jar
    ```
    You can see the app starts in few hundred milliseconds.
    
3. To test the `HelloController` you created, either `curl http://localhost:8080/hello` or open it in a browser:
    ```bash
    curl http://localhost:8080/hello
    ```

    The response should be `Example Response`. 
    Then stop the application:
    ```
    CTRL-C
    ```

## Build and Run a Native Executable

With no runtime reflection, Micronaut is extremely well suited to ahead-of-time (AOT) compilation with [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/).
Mironaut provides support for GraalVM Native Image by default.

1. Compile this REST application ahead of time with Gradle:
    ```bash
    ./gradlew nativeCompile
    ```
    
    Compilation can take a few minutes, but more cores and more memory reduce the required time.

    The result is a standalone executable placed in the _build/native/nativeCompile/_ directory named `MnHelloRest`.

2. Run the application from the native executable:
    ```bash
    ./build/native/nativeCompile/MnHelloRest
    ```
    This ahead-of-time compiled application started much faster than when running on HorSpot!

    It is so fast because it does not have to parse bytecode for JDK and application classes, initialize the JIT compiler, allocate JIT code caches, JIT profile data caches, and so on.
    The startup is negligible.

## Containerize and Run in Docker on Linux

If you are on Linux, you can easily create a Docker container image that includes the native Linux executable you have built.
For Windows or macOS, the process is little different and is not covered here.

Typically, the first question is what base image to use? 
GraalVM Native Image supports both static and dynamically linked executables, with dynamic being the default.
Your native executable is dynamically linked against `glibc`, you will need a base image that includes it.
One of the smallest base images you could use is Alpine Linux with `glibc`.

1. Create a `Dockerfile` with the following contents:
    ```Dockerfile
    FROM frolvlad/alpine-glibc:alpine-3.12
    EXPOSE 8080
    ADD build/native-image/application /app/example
    ENTRYPOINT ["/app/example"]
    ```

    Simply copy the native executable into the container image and expose port 8080.

2. Build a container image:
    ```bash
    docker build . -t example
    ```

    The container image that was created is about 94MB, which makes sense because the Alpine with `glibc` base image is about 18MB and your application is about 71MB.
    ```bash
    docker images
    ```
    ```sh
    REPOSITORY              TAG                 IMAGE ID            CREATED             SIZE
    hello                   latest              f5ea290d8d08        2 seconds ago       94.2MB
    ```

3. You can run the container image and the application directly:
    ```bash
    docker run -p8080:8080 --rm hello
    ```
    ```bash
    curl http://localhost:8080/hello
    ```
    It will return the same message "Example Response".

### Wrapping Up

Micronaut makes it really easy to build modern Java applications and microservices.
Its elimination of runtime reflection also makes it an ideal application framework to use with GraalVM Native Image for ahead-of-time compilation and containerization.