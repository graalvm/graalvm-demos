# Micronaut REST Application Using Maven and GraalVM Native Image

This example shows how to run a simple [Micronaut](https://micronaut.io/) REST application, compile it with [GraalVM Native Image](https://www.graalvm.org/reference-manual/native-image/), and package it in a container image.
Along the way you will see the performance benefits that GraalVM Native Image provides to Micronaut applications.

## Preparation

1. Download and install GraalVM using [SDKMAN!](https://sdkman.io/):
    ```bash
    sdk install java 21.0.5-graal
    ```

2. Download or clone GraalVM demos repository and navigate into the example directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-image/microservices/micronaut-hello-rest-maven
    ```

## Compile and Run from a JAR File

1. Compile the application and build a fat JAR that includes all its dependencies:
    ```bash
    ./mvnw clean package
    ```

2. Run the application on HotSpot from a JAR file:
    ```bash
    java -jar target/MnHelloRest-0.1.jar
    ```
    You can see the app starts in few hundred milliseconds.
    
3. To test the `HelloController` you created, either `curl http://localhost:8080/GraalVM` or open it in a browser:
    ```bash
    curl http://localhost:8080/GraalVM
    ```

    The response should be `Hello GraalVM`. 
    Then stop the application:
    ```
    CTRL-C
    ```

## Build and Run a Native Executable

With no runtime reflection, Micronaut is extremely well suited to ahead-of-time (AOT) compilation with [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/).
Micronaut provides support for GraalVM Native Image by default.

1. Compile this REST application ahead of time:
    ```bash
    ./mvnw package -Dpackaging=native-image
    ```
    
    Compilation can take a few minutes. Compiling on machine with more cores and more memory will reduce the build time.

    The result is a standalone executable placed in the _target/_ directory named `MnHelloRest`.

2. Run the application from the native executable:
    ```bash
    ./target/MnHelloRest
    ```
    This ahead-of-time compiled application started much faster than when running as a JAR!

    It is so fast because it does not have to parse bytecode for JDK and application classes, initialize the JIT compiler, allocate JIT code caches, JIT profile data caches, and so on.

3. To test the `HelloController` you created, either `curl http://localhost:8080/GraalVM` or open it in a browser:
    ```bash
    curl http://localhost:8080/GraalVM
    ```

    The response should be `Hello GraalVM`. 
    Then stop the application:
    ```
    CTRL-C
    ```

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
    curl http://localhost:8080/GraalVM
    ```
    It will return the same message "Hello GraalVM".

### Wrapping Up

Micronaut makes it really easy to build modern Java applications and microservices.
Its elimination of runtime reflection also makes it an ideal application framework to use with GraalVM Native Image for ahead-of-time compilation and containerization.