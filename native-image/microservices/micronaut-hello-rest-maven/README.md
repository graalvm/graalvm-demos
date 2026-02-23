# Micronaut REST Application Using Maven and GraalVM Native Image

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
Micronaut provides support for GraalVM Native Image by default.

1. Compile this REST application ahead of time:
    ```bash
    ./mvnw package -Dpackaging=native-image
    ```

    Compilation can take a few minutes. Compiling on a machine with more cores and more memory will reduce the build time.

    The result is a standalone executable placed in the _target/_ directory named `hello`.

2. Run the application from the native executable:
    ```bash
    ./target/hello
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

You can run the same steps in containers using Docker.
A common first question is which base container image to use.
GraalVM Native Image supports both statically and dynamically linked executables, with dynamic linking being the default.
To run a native executable that is dynamically linked against `glibc`, you need a base image that includes it.
A good choice is a Google Distroless image, such as `java-base-debian13`.

1. Create a `Dockerfile.native` with the following contents:
    ```Dockerfile
    # Builder
    FROM container-registry.oracle.com/graalvm/native-image:25 AS nativebuild
    WORKDIR /
    COPY . .
    RUN chmod +x mvnw
    RUN ./mvnw --no-transfer-progress package -Dpackaging=native-image

    # Runner
    FROM gcr.io/distroless/java-base-debian13
    COPY --from=nativebuild /target/hello /
    EXPOSE 8080
    ENTRYPOINT ["/hello"]
    ```

    This example demonstrates a multi-stage build.
    First, a dynamically linked native executable is built using the `graalvm/native-image:25` container image as the builder.
    The executable is then copied into the runner container, where port 8080 is exposed and the entrypoint is configured. The runtime image is based on Debian and provides `glibc` and other libraries required to run the native executable, without including a full JDK installation.
    This is sufficient to run the native image.

2. Build a container image:
    ```bash
    docker build . -f Dockerfile.native -t micronaut:hello
    ```
    Check the file size of the newly-created image:
    ```bash
    docker images
    ```
    ```
    REPOSITORY   TAG         IMAGE ID       CREATED             SIZE
    micronaut    hello       6b7ba89420ae   31 minutes ago      56.9MB
    ```

    The container image that was created is about 56.9MB.

3. Now run the container image with `docker`:
    ```bash
    docker run -p8080:8080 --rm micronaut:hello
    ```

4. Finally, test the application using `curl` in the second terminal window or open it in a browser.
    ```bash
    curl http://localhost:8080/GraalVM
    ```
    It returns the same message: "Hello GraalVM".

Additionally, you can find an example Dockerfile, _Dockerfile.jvm_, for packaging and running this Micronaut application from a JAR file.
You can build the second container image and compare the sizes of both images:
```bash
docker build . -f Dockerfile.jvm -t micronaut:hello.jvm
```
```bash
docker images | grep micronaut

REPOSITORY   TAG         IMAGE ID       CREATED          SIZE
micronaut    hello       b10fb62f7b3e   4 seconds ago    56.9MB
micronaut    hello.jvm   7471e8f472f8   36 minutes ago   245MB
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
