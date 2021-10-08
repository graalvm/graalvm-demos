# Micronaut with GraalVM Native Image and Docker

This example shows how to create a simple [Micronaut](https://micronaut.io/) REST application, compile it with [GraalVM Native Image](https://www.graalvm.org/reference-manual/native-image/), and package it in a Docker container image.
Along the way we'll also take a look at some of the performance benefits that Native Image provides to Micronaut applications.

As you make your way through this example, look out for this icon:
![](keyboard.jpg)
Whenever you see it, it's time for you to perform an action.

## Environment Setup

To run this example you'll need GraalVM and the Micronaut CLI installed on a machine with Docker.

### Download the latest release of GraalVM

You can use either [GraalVM Enterprise](https://www.oracle.com/downloads/graalvm-downloads.html) or [GraalVM Community](https://www.graalvm.org/downloads).
GraalVM Enterprise offers smaller and faster applications.

### Install GraalVM Native Image

GraalVM comes with `gu` which is a command line utility to install and manage additional functionalities.
To install Native Image, run this single command:

![](keyboard.jpg) `gu install native-image`

NOTE: If you are using GraalVM Enterprise, you will be asked to accept the licence agreement.

### Install Micronaut

You can download Micronaut and [install it manually](https://micronaut.io/download) or you can use one of the popular package managers:
* Linux: [SDKman instructions](https://micronaut-projects.github.io/micronaut-starter/latest/guide/index.html#installSdkman)
* macOS: [Homebrew instructions](https://micronaut-projects.github.io/micronaut-starter/latest/guide/index.html#installHomebrew)
* Windows: [Chocolatey instructions](https://micronaut-projects.github.io/micronaut-starter/latest/guide/index.html#installChocolatey)

NOTE: If you've previously installed Micronaut you should upgrade to the latest release (3.x) to ensure it includes GraalVM Native Image support.

## Running the Example

### Create the Application

To avoid lots of typing, we can use the Micronaut `mn` CLI to create a sample application:

![](keyboard.jpg) `mn create-app hello`

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

Let's `cd` into this directory and add a rest endpoint that will return a simple message.

![](keyboard.jpg) <br>
`cd hello`<br>
`mn create-controller hello`

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

We won't dwell on application details here, but Micronaut has [great docs and samples](https://micronaut.io/docs/) where you can learn more.

### Compiling and running on GraalVM JIT

Use Gradle to compile the application code and build a fat JAR that includes all its dependencies:

![](keyboard.jpg) `./gradlew assemble`

When complete, let's launch the app using the `java -jar` command which will start up a web server on the HotSpot JVM.

![](keyboard.jpg) `java -jar build/libs/hello-0.1-all.jar`

You can see the app starts in few hundred milliseconds.  How many will depend on the speed of your machine.
In this run it took 937ms, almost one second, to boot up:

```sh
__  __ _                                  _
|  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_
| |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
| |  | | | (__| | | (_) | | | | (_| | |_| | |_
|_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|
 Micronaut (v3.0.0)

22:16:29.972 [main] INFO  i.m.context.env.DefaultEnvironment - Established active environments: [oraclecloud, cloud]
22:16:30.800 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 937ms. Server Running: http://micronautexample:8080
```

To exercise the `HelloController` we created, either `curl http://localhost:8080/hello` or open it in a browser:

![](keyboard.jpg) `curl http://localhost:8080/hello`

The response should be `Example Response`. Stop the application and we'll continue on to native executable generation.

![](keyboard.jpg) `CTRL-C`

### Compiling with GraalVM Native Image

With no runtime reflection, Micronaut is extremely well suited to ahead-of-time (AOT) compilation with GraalVM Native Image.
It even includes build support for Native Image in Gradle and Maven projects created by `mn` so we can compile with a single command:

![](keyboard.jpg) `./gradlew nativeImage`

Compilation can take a few minutes, but more cores and more memory reduces the required time!

```sh
> Task :compileJava
Note: Creating bean classes for 1 type elements

> Task :generateResourceConfigFile
Generating /Users/ogupalo/graalvm-demos/hello/build/generated/resources/graalvm/resource-config.json

> Task :nativeImage
[application:31406]    classlist:   2,662.33 ms,  1.18 GB
[application:31406]        (cap):     803.97 ms,  1.18 GB
[application:31406]        setup:   3,238.24 ms,  1.18 GB
[application:31406]     (clinit):   1,161.24 ms,  5.36 GB
[application:31406]   (typeflow):  15,410.49 ms,  5.36 GB
[application:31406]    (objects):  22,238.96 ms,  5.36 GB
[application:31406]   (features):   2,381.91 ms,  5.36 GB
[application:31406]     analysis:  42,972.27 ms,  5.36 GB
[application:31406]     universe:   2,147.73 ms,  5.36 GB
[application:31406]      (parse):   3,262.36 ms,  5.51 GB
[application:31406]     (inline):   7,580.11 ms,  6.98 GB
[application:31406]    (compile):  67,779.11 ms,  6.79 GB
[application:31406]      compile:  83,760.95 ms,  6.79 GB
[application:31406]        image:   7,023.99 ms,  6.78 GB
[application:31406]        write:   1,293.61 ms,  6.78 GB
[application:31406]      [total]: 143,368.15 ms,  6.78 GB
# Printing build artifacts to: /Users/ogupalo/graalvm-demos/hello/build/native-image/application.build_artifacts.txt
Native Image written to: /Users/ogupalo/graalvm-demos/hello/build/native-image/application

BUILD SUCCESSFUL in 2m 26s
3 actionable tasks: 2 executed, 1 up-to-date
```

The result is a 62M standalone executable placed in the `build/native-image` folder with the very generic default name `application`.

![](keyboard.jpg) `ls -lh build/native-image`

```sh
total 126624
-rwxr-xr-x  1 ogupalo  staff    62M Oct  8 14:24 application
-rw-r--r--  1 ogupalo  staff    26B Oct  8 14:24 application.build_artifacts.txt
```

Let's startup the application.  It's a native executable, so we just invoke it:

![](keyboard.jpg) `./build/native-image/application`

```sh
__  __ _                                  _
|  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_
| |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
| |  | | | (__| | | (_) | | | | (_| | |_| | |_
|_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|
 Micronaut (v3.0.0)

14:50:05.985 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 54ms. Server Running: http://localhost:8080
```

Running on the same machine as before, the ahead-of-time compiled app boots and is listening on port 8080 in 54ms!
Thats's much than when running on the JVM.

It's so fast because it doesn't have to do many of the boot-time tasks that the HotSpot JVM has to do like parsing bytecode for JDK and application classes, initialize the JIT compiler, allocating JIT code caches, JIT profile data caches, etc.
With a GraalVM native executable the application startup cost is neglible.

### Docker on Linux

If you're on Linux, you can easily create a Docker container image that includes the native Linux executable you've built.
If you're on Windows or macOS, the process is little different and isn't covered here.

Typically, the first question is what base image to use? GraalVM Native Image supports both static and dynamically linked executables, with dynamic being the default.
So as our native executable is dynamically linked against `glibc`, we'll need a base image that includes it.
One of the smallest base images we could use is Alpine Linux with `glibc`.

![](keyboard.jpg) create a `Dockerfile` with the following contents:

```Dockerfile
FROM frolvlad/alpine-glibc:alpine-3.12
EXPOSE 8080
ADD build/native-image/application /app/hello
ENTRYPOINT ["/app/hello"]
```

We simply copy the native executable into the container image and expose port 8080--that's it!
Let's build a container image:

![](keyboard.jpg) `docker build . -t hello`

```sh
Sending build context to Docker daemon    158MB
Step 1/4 : FROM  frolvlad/alpine-glibc:alpine-3.12
 ---> d955957758ab
Step 2/4 : EXPOSE 8080
 ---> Running in 00d505075680
Removing intermediate container 00d505075680
 ---> 87c36670959f
Step 3/4 : ADD build/native-image/application /app/hello
 ---> 347c2b8ffe24
Step 4/4 : ENTRYPOINT ["/app/hello"]
 ---> Running in 2fb1bbeae21b
Removing intermediate container 2fb1bbeae21b
 ---> f5ea290d8d08
Successfully built f5ea290d8d08
Successfully tagged hello:latest
```

We can see the container image we built is about 94MB, which makes sense because the Alpine with `glibc` base image is about 18MB and our application binary is about 71MB.

![](keyboard.jpg) `docker images`

```sh
REPOSITORY              TAG                 IMAGE ID            CREATED             SIZE
hello                   latest              f5ea290d8d08        2 seconds ago       94.2MB
frolvlad/alpine-glibc   alpine-3.12         d955957758ab        3 months ago        17.9MB
```

We can run the container image and use it just like we did when running the application directly:

![](keyboard.jpg) `docker run -p8080:8080 --rm hello`

![](keyboard.jpg) `curl http://localhost:8080/hello`

```sh
Example Response
```

### Wrapping Up

Micronaut makes it really easy to build modern Java applications and microservices.
Its elimination of runtime reflection also makes it the ideal application framework for use with GraalVM Native Image for ahead of time compilation and containerization.
