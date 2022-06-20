# Micronaut with GraalVM Native Image and Docker

This example shows how to create a simple [Micronaut](https://micronaut.io/) REST application, compile it with [GraalVM Native Image](https://www.graalvm.org/reference-manual/native-image/), and package it in a Docker container image.
Along the way we'll also take a look at some of the performance benefits that Native Image provides to Micronaut applications.

As you make your way through this example, look out for this icon:
![](keyboard.jpg)
Whenever you see it, it's time for you to perform an action.

## Environment Setup

To run this example you'll need GraalVM and the Micronaut CLI installed on a machine with Docker.

### Download the Latest GraalVM

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
In this run it took 557ms, about half a second, to boot up:

```sh
 __  __ _                                  _   
|  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_ 
| |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
| |  | | | (__| | | (_) | | | | (_| | |_| | |_ 
|_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|
  Micronaut (v3.5.1)

22:50:31.973 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 557ms. Server Running: http://localhost:8080

```

To exercise the `HelloController` we created, either `curl http://localhost:8080/hello` or open it in a browser:

![](keyboard.jpg) `curl http://localhost:8080/hello`

The response should be `Example Response`. Stop the application and we'll continue on to native executable generation.

![](keyboard.jpg) `CTRL-C`

## Compile with GraalVM Native Image

With no runtime reflection, Micronaut is extremely well suited to ahead-of-time (AOT) compilation with GraalVM Native Image.
It even includes build support for Native Image in Gradle and Maven projects created by `mn` so we can compile with a single command:

![](keyboard.jpg) `./gradlew nativeBuild`

Compilation can take a few minutes, but more cores and more memory reduces the required time!

```
> Task :generateResourcesConfigFile
[native-image-plugin] Resources configuration written into /home/rleland/Projects/GitHub/graalvm/graalvm-demos/micronaut-nativeimage/hello/build/native/generated/generateResourcesConfigFile/resource-config.json

> Task :nativeCompile
[native-image-plugin] Toolchain detection is disabled, will use GraalVM from /home/rleland/.local/share/graalvm/graalvm.
[native-image-plugin] Using executable path: /home/rleland/.local/share/graalvm/graalvm/bin/native-image
Warning: Using a deprecated option --allow-incomplete-classpath from 'META-INF/native-image/io.micronaut/micronaut-inject/native-image.properties' in 'file:///home/rleland/.gradle/caches/modules-2/files-2.1/io.micronaut/micronaut-inject/3.5.1/cc9e726fe97c46286e5635015d74c2b89096b790/micronaut-inject-3.5.1.jar'. Allowing an incomplete classpath is now the default. Use --link-at-build-time to report linking errors at image build time for a class or package.
========================================================================================================================
GraalVM Native Image: Generating 'hello' (executable)...
========================================================================================================================
[1/7] Initializing...                                                                                    (2.8s @ 0.28GB)
 Version info: 'GraalVM 22.1.0 Java 11 CE'
 C compiler: gcc (linux, x86_64, 11.2.0)
 Garbage collector: Serial GC
 4 user-provided feature(s)
  - io.micronaut.buffer.netty.NettyFeature
  - io.micronaut.core.graal.ServiceLoaderFeature
  - io.micronaut.http.netty.graal.HttpNettyFeature
  - io.micronaut.jackson.JacksonDatabindFeature
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type kotlinx.coroutines.reactive.ReactiveFlowKt not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type io.netty.channel.kqueue.KQueue not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type org.apache.logging.log4j.core.config.Configurator not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type io.netty.channel.kqueue.KQueue not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type io.netty.channel.epoll.Epoll not present
Warning: Could not register complete reflection metadata for io.micronaut.http.bind.binders.ContinuationArgumentBinder. Reason(s): java.lang.TypeNotPresentException: Type kotlin.coroutines.Continuation not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type kotlin.coroutines.CoroutineContext not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type io.netty.channel.epoll.Epoll not present
[2/7] Performing analysis...  [***************]                                                         (23.8s @ 2.36GB)
  13,107 (91.99%) of 14,248 classes reachable
  18,653 (59.12%) of 31,551 fields reachable
  65,802 (62.98%) of 104,483 methods reachable
     702 classes,   242 fields, and 2,414 methods registered for reflection
      68 classes,    88 fields, and    55 methods registered for JNI access
[3/7] Building universe...                                                                               (2.0s @ 3.28GB)
[4/7] Parsing methods...      [*]                                                                        (1.0s @ 4.46GB)
[5/7] Inlining methods...     [****]                                                                     (1.3s @ 5.05GB)
[6/7] Compiling methods...    [****]                                                                    (12.0s @ 2.81GB)
[7/7] Creating image...                                                                                  (3.0s @ 4.06GB)
  24.53MB (42.25%) for code area:   43,766 compilation units
  25.56MB (44.03%) for image heap:   9,362 classes and 297,458 objects
   7.97MB (13.72%) for other data
  58.06MB in total
------------------------------------------------------------------------------------------------------------------------
Top 10 packages in code area:                               Top 10 object types in image heap:
   1.60MB sun.security.ssl                                     5.36MB byte[] for code metadata
1006.64KB java.util                                            3.72MB java.lang.Class
 688.46KB com.sun.crypto.provider                              2.52MB java.lang.String
 573.56KB reactor.core.publisher                               2.38MB byte[] for java.lang.String
 560.76KB io.netty.buffer                                      2.37MB byte[] for general heap data
 542.04KB com.oracle.svm.core.reflect                          1.20MB com.oracle.svm.core.hub.DynamicHubCompanion
 497.99KB sun.security.x509                                  615.35KB byte[] for reflection metadata
 479.24KB io.netty.handler.codec.http2                       572.55KB java.lang.String[]
 463.12KB java.util.concurrent                               520.08KB java.util.HashMap$Node
 452.63KB java.lang                                          513.44KB java.lang.reflect.Method
      ... 478 additional packages                                 ... 2912 additional object types
                                           (use GraalVM Dashboard to see all)
------------------------------------------------------------------------------------------------------------------------
                        5.1s (10.2% of total time) in 31 GCs | Peak RSS: 7.90GB | CPU load: 8.78
------------------------------------------------------------------------------------------------------------------------
Produced artifacts:
 /home/rleland/Projects/GitHub/graalvm/graalvm-demos/micronaut-nativeimage/hello/build/native/nativeCompile/hello (executable)
 /home/rleland/Projects/GitHub/graalvm/graalvm-demos/micronaut-nativeimage/hello/build/native/nativeCompile/hello.build_artifacts.txt
========================================================================================================================
Finished generating 'hello' in 49.7s.
[native-image-plugin] Native Image written to: /home/rleland/Projects/GitHub/graalvm/graalvm-demos/micronaut-nativeimage/hello/build/native/nativeCompile

BUILD SUCCESSFUL in 51s
5 actionable tasks: 2 executed, 3 up-to-date

```

The result is a 54M standalone executable placed in the `build/native/nativeCompile` folder named 'hello`.
    
    For Micronaught 3.0 and an older GraalVM distribution this executable was 63M, which is a 14% reduction in size!

![](keyboard.jpg) `ls -lh build/native/nativeCompile`

```sh
total 55340
-rwxrwxr-x 1 rleland rleland 56647944 Jun 19 22:38 hello
-rw-rw-r-- 1 rleland rleland       20 Jun 19 22:38 hello.build_artifacts.txt
```

Let's startup the application.  It's a native executable, so we just invoke it:

![](keyboard.jpg) `./build/native/nativeCompile/hello`

```sh
 __  __ _                                  _   
|  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_ 
| |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
| |  | | | (__| | | (_) | | | | (_| | |_| | |_ 
|_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|
  Micronaut (v3.5.1)

22:48:45.066 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 28ms. Server Running: http://localhost:8080
```
Running on the same machine as before, the ahead-of-time compiled app boots and is listening on port 8080 in 28ms!
Thats's much faster than when running on the JVM.

It's so fast because it doesn't have to do many of the boot-time tasks that the HotSpot JVM has to do like parsing bytecode for JDK and application classes, initialize the JIT compiler, allocating JIT code caches, JIT profile data caches, etc.
With a GraalVM native executable the application startup cost is neglible.

## Docker on Linux

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
