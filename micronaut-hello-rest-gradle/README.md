# Micronaut with GraalVM Native Image and Docker

This example shows how to create a simple [Micronaut](https://micronaut.io/) REST application, compile it with [GraalVM Native Image](https://www.graalvm.org/reference-manual/native-image/), and package it in a Docker container image.
Along the way you will see the performance benefits that GraalVM Native Image provides to Micronaut applications.

## Preparation

To run this example you will need GraalVM and the Micronaut CLI installed on a machine with Docker.

1. Download and install GraalVM for JDK 17 using [SDKMAN!](https://sdkman.io/).
    ```bash
    sdk install java 17.0.9-graal
    ```
    
2. Install Micronaut:

    You can download Micronaut and [install it manually](https://micronaut.io/download) or you can use one of the popular package managers:
    * Linux: [SDKman instructions](https://micronaut-projects.github.io/micronaut-starter/latest/guide/index.html#installSdkman)
    * macOS: [Homebrew instructions](https://micronaut-projects.github.io/micronaut-starter/latest/guide/index.html#installHomebrew)
    * Windows: [Chocolatey instructions](https://micronaut-projects.github.io/micronaut-starter/latest/guide/index.html#installChocolatey)

    NOTE: If you've previously installed Micronaut you should upgrade to the latest release (3.x).

3. Download or clone GraalVM demos repository and navigate into the `micronaut-hello-rest-gradle` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/micronaut-hello-rest-gradle
    ```

## Running the Example

### Create the Application

Use the Micronaut `mn` CLI to create a sample application:
```bash
mn create-app hello
```

This will create a `hello` folder with a complete Micronaut application skeleton.
It uses Gradle by default, but you can create a Maven project using the appropriate `mn` command line args.
```sh
└── hello
    ├── build.gradle
    ├── gradle
    │   └── wrapper
    │       ├── gradle-wrapper.jar
    │       └── gradle-wrapper.properties
    ├── gradle.properties
    ├── gradlew
    ├── gradlew.bat
    ├── micronaut-cli.yml
    ├── README.md
    ├── settings.gradle
    └── src
        ├── main
        │   ├── java
        │   │   └── hello
        │   │       └── Application.java
        │   └── resources
        │       ├── application.yml
        │       └── logback.xml
        └── test
            └── java
                └── hello
                    └── HelloTest.java
```

Navigate into the application directory and add a rest endpoint that returns a simple message.
```bash
cd hello
```
```bash
mn create-controller hello
```

This creates a `HelloController` that exposes an endpoint at `/hello`:
```java
package hello;

import io.micronaut.http.annotation.*;

@Controller("/hello")
public class HelloController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}
```

### Compile and Run on GraalVM JIT

Use Gradle to compile the application code and build a fat JAR that includes all its dependencies:
```bash
./gradlew assemble
```

When complete, start the app using the `java -jar` command which will start up a web server on the HotSpot JVM.
```bash
java -jar build/libs/hello-0.1-all.jar
```

You can see the app starts in few hundred milliseconds.  How many will depend on the speed of your machine.

To exercise the `HelloController` you created, either `curl http://localhost:8080/hello` or open it in a browser:
```bash
curl http://localhost:8080/hello
```

The response should be `Example Response`. Stop the application:
```
CTRL-C
```

## Compile with GraalVM Native Image

With no runtime reflection, Micronaut is extremely well suited to ahead-of-time (AOT) compilation with [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/).
It even includes build support for Native Image in Gradle and Maven projects created by `mn` so you can compile with a single command:
```bash
./gradlew nativeCompile
```
  
Compilation can take a few minutes, but more cores and more memory reduce the required time.

The result is a 54M standalone executable placed in the `build/native/nativeCompile` folder named 'hello`.
```bash
ls -lh build/native/nativeCompile
```

Run the application from the native executable:
```bash
./build/native/nativeCompile/hello
```
The ahead-of-time compiled application started much faster than when running on the JVM!

It is so fast because it does not have to parse bytecode for JDK and application classes, initialize the JIT compiler, allocate JIT code caches, JIT profile data caches, etc.
With a GraalVM native executable the application startup cost is neglible.

## Containerize and Run in Docker on Linux

If you are on Linux, you can easily create a Docker container image that includes the native Linux executable you have built.
For Windows or macOS, the process is little different and is not covered here.

Typically, the first question is what base image to use? 
GraalVM Native Image supports both static and dynamically linked executables, with dynamic being the default.
Your native executable is dynamically linked against `glibc`, you will need a base image that includes it.
One of the smallest base images you could use is Alpine Linux with `glibc`.

Create a `Dockerfile` with the following contents:
```Dockerfile
FROM frolvlad/alpine-glibc:alpine-3.12
EXPOSE 8080
ADD build/native-image/application /app/hello
ENTRYPOINT ["/app/hello"]
```

Simply copy the native executable into the container image and expose port 8080.
Build a container image:
```bash
docker build . -t hello
```

The container image that was created is about 94MB, which makes sense because the Alpine with `glibc` base image is about 18MB and your application is about 71MB.
```bash
doker images
```
```sh
REPOSITORY              TAG                 IMAGE ID            CREATED             SIZE
hello                   latest              f5ea290d8d08        2 seconds ago       94.2MB
```

You can run the container image and the application directly:
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