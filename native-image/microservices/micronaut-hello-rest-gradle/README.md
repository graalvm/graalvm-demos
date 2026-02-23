# Micronaut REST Application Using Gradle and GraalVM Native Image

This example shows how to run a simple [Micronaut](https://micronaut.io/) REST application, compile it with [GraalVM Native Image](https://www.graalvm.org/reference-manual/native-image/), and package it in a container image.
Along the way you will see the performance benefits that GraalVM Native Image provides to Micronaut applications.

## Preparation

1. Download and install GraalVM using [SDKMAN!](https://sdkman.io/):
    ```bash
    sdk install java 25-graal
    ```

2. Download or clone GraalVM demos repository and navigate into the example directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-image/microservices/micronaut-hello-rest-gradle
    ```

## Compile and Run from a JAR File

1. Compile the application:
    ```bash
    ./gradlew build
    ```

2. Run the application on the JVM:
    ```bash
    ./gradlew run
    ```
    You can see the app starts in few hundred milliseconds.

3. To test the application, either send a `curl` request from a new terminal window, or open it in a browser:
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
Mironaut provides support for GraalVM Native Image by default.

1. Compile this REST application ahead of time with Gradle:
    ```bash
    ./gradlew nativeCompile
    ```

    Compilation can take a few minutes, but more cores and more memory reduce the required time.

    The result is a standalone executable placed in the _build/native/nativeCompile/_ directory named `hello`.

2. Run the application from the native executable:
    ```bash
    ./build/native/nativeCompile/hello
    ```
    This ahead-of-time compiled application started much faster than when running on the JVM!

    It is so fast because it does not have to parse bytecode for JDK and application classes, initialize the JIT compiler, allocate JIT code caches, JIT profile data caches, and so on.

3. Test the application either with `curl` or open it in a browser:
    ```bash
    curl http://localhost:8080/GraalVM
    ```
    It returns the same message: "Hello GraalVM".
    Then stop the application:
    ```
    CTRL-C
    ```

## Containerize and Run in Docker

You can run this application in containers using Docker.
There are two ways to achieve this: using the default Micronaut container tasks or creating custom containers.

### Default Containers

The Micronaut Gradle plugin, `io.micronaut.application`, provides tasks to build container images for your application without requiring you to write a Dockerfile.

* The first task is:
    ```bash
    ./gradlew dockerBuild
    ```
    This task builds an optimized container image that runs your application on the JVM.
    **Docker pulls a Java runtime image base image compatible with your JDK version (Java 25 in this example).**
    As a result, you get the `hello:latest` image (with the expected file size 323MB) that you can run with:
    ```bash
    docker run -p 8080:8080 hello:latest
    ```

* The second task is:
    ```bash
    ./gradlew dockerBuildNative
    ```
    This task builds a container image that runs a GraalVM Native Image executable instead of a JAR file.
    The container includes: your compiled native executable, a minimal OS image, no JVM.
    The image tag and name remain the same, and you can run it as before:

    ```bash
    docker run -p 8080:8080 hello:latest
    ```
    Startup is significantly faster compared to the JVM-based container.

### Custom Containers

If you need to use different base images than those provided by the Micronaut Gradle plugin, you can write a custom Dockerfile and control both the builder and runtime stages.

For example, when using GraalVM Native Image, you can create either statically or dynamically linked executables. Dynamic linking is the default.

To run a native executable dynamically linked against `glibc`, you need a base image that provides it.
A good choice is a Google Distroless image such as **gcr.io/distroless/java-base-debian13**.

Below is an example of deploying this application using a native executable in a custom container.

1. Create a _Dockerfile.native_ with the following contents:
    ```Dockerfile.native
    # Builder
    FROM container-registry.oracle.com/graalvm/native-image:25 AS nativebuild
    WORKDIR /build
    COPY . .
    RUN ./gradlew nativeCompile

    # Runner
    FROM gcr.io/distroless/java-base-debian13
    COPY --from=nativebuild /build/build/native/nativeCompile/hello /hello
    EXPOSE 8080
    ENTRYPOINT ["/hello"]
    ```

    This example demonstrates a multi-stage build.

    First, the executable is built using the `graalvm/native-image:25` container image as the builder.
    Then the executable is copied into a minimal runtime container, where port 8080 is exposed and the entrypoint is configured.

    The runtime image is based on Debian and provides `glibc` and other required system libraries, without including a full JDK installation.

2. Build a container image:
    ```bash
    docker build . -f Dockerfile.native -t micronaut:hello
    ```
    Check the file size of the newly-created image:
    ```bash
    docker images | grep micronaut
    ```
    ```
    REPOSITORY   TAG         IMAGE ID       CREATED             SIZE
    micronaut    hello       6b7ba89420ae   31 minutes ago      102MB
    ```

    The container image that was created is about 100MB.

3. Then run this container image with `docker`:
    ```bash
    docker run -p8080:8080 --rm micronaut:hello
    ```
    You can test the application using `curl` or open it in a browser (same as before).

Additionally, an example Dockerfile named _Dockerfile.jvm_ is included for running this Micronaut application on the JVM in a custom container.
You can build and compare both images:
```bash
docker build . -f Dockerfile.jvm -t micronaut:hello.jvm
```
```bash
docker images | grep micronaut
```
```
REPOSITORY   TAG         IMAGE ID       CREATED          SIZE
micronaut    hello.jvm   f335f24cb16f   About a minute ago   245MB
micronaut    hello       cf315cc746c4   7 minutes ago        102MB
```
The difference in image size is significant.
This is because a native container does not include a full JVM; it contains only the application code and the minimal runtime required to execute it.

### Wrapping Up

Micronaut makes it really easy to build modern Java applications and microservices.
Its elimination of runtime reflection also makes it an ideal application framework to use with GraalVM Native Image for ahead-of-time compilation and containerization.

To take this further, you can turn your application into a completely static or mostly statically linked native executable using Native Image.
In this case, the application can run even in minimal or empty containers such as `scratch` or Alpine Linux.

See the guide [Build a Statically Linked or Mostly-Statically Linked Native Executable](https://www.graalvm.org/latest/reference-manual/native-image/guides/build-static-executables/) to learn more.

### Related Documentation

- [Tiny Java Containers](../../tiny-java-containers/) demo shows how a simple Java application and a simple web server can be compiled to produce very small Docker container images using various lightweight base images.
- [From JIT to Native: Efficient Java Containers with GraalVM and Spring Boot](https://github.com/graalvm/workshops/tree/main/native-image/spring-boot-webserver) workshop demonstrates how to build efficient, size-optimized native applications, and deploy them in various containers.
