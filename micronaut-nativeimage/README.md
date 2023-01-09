# Micronaut with GraalVM Native Image and Docker

This example shows how to create a simple [Micronaut](https://micronaut.io/) REST application, compile it with [GraalVM Native Image](https://www.graalvm.org/reference-manual/native-image/), and package it in a Docker container image.
Along the way we'll also take a look at some of the performance benefits that Native Image provides to Micronaut applications.

As you make your way through this example, look out for this icon:
![](keyboard.jpg)
Whenever you see it, it's time for you to perform an action.

## Preparation

To run this example you'll need GraalVM and the Micronaut CLI installed on a machine with Docker.

1. Download and install the latest GraalVM JDK using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader). 
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) 
    ```
    
2. Install Micronaut:

    You can download Micronaut and [install it manually](https://micronaut.io/download) or you can use one of the popular package managers:
    * Linux: [SDKman instructions](https://micronaut-projects.github.io/micronaut-starter/latest/guide/index.html#installSdkman)
    * macOS: [Homebrew instructions](https://micronaut-projects.github.io/micronaut-starter/latest/guide/index.html#installHomebrew)
    * Windows: [Chocolatey instructions](https://micronaut-projects.github.io/micronaut-starter/latest/guide/index.html#installChocolatey)

    NOTE: If you've previously installed Micronaut you should upgrade to the latest release (3.x) to ensure it includes GraalVM Native Image support.

3. Download or clone GraalVM demos repository and navigate into the `micronaut-nativeimage` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/micronaut-nativeimage
    ```
   NOTE: If you are using GraalVM Enterprise, you will be asked to accept the licence agreement.

## Running the Example

### Create the Application

To avoid lots of typing, we can use the Micronaut `mn` CLI to create a sample application:

![](keyboard.jpg) 
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

Let's `cd` into this directory and add a rest endpoint that will return a simple message.

![](keyboard.jpg) <br>
`cd hello`<br>
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

We won't dwell on application details here, but Micronaut has [great docs and samples](https://micronaut.io/docs/) where you can learn more.

### Compiling and running on GraalVM JIT

Use Gradle to compile the application code and build a fat JAR that includes all its dependencies:

![](keyboard.jpg)
```bash
./gradlew assemble
```

When complete, let's launch the app using the `java -jar` command which will start up a web server on the HotSpot JVM.

![](keyboard.jpg) 
```bash
java -jar build/libs/hello-0.1-all.jar
```

You can see the app starts in few hundred milliseconds.  How many will depend on the speed of your machine.
In this run it took 486ms, about half a second, to boot up:

```sh
 __  __ _                                  _   
|  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_ 
| |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
| |  | | | (__| | | (_) | | | | (_| | |_| | |_ 
|_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|
  Micronaut (v3.5.2)

22:21:47.239 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 486ms. Server Running: http://localhost:8080
```
To exercise the `HelloController` we created, either `curl http://localhost:8080/hello` or open it in a browser:

![](keyboard.jpg)
```bash
curl http://localhost:8080/hello
```

The response should be `Example Response`. Stop the application and we'll continue on to native executable generation.

![](keyboard.jpg) 
```
CTRL-C
```

## Compile with GraalVM Native Image

With no runtime reflection, Micronaut is extremely well suited to ahead-of-time (AOT) compilation with GraalVM Native Image.
It even includes build support for Native Image in Gradle and Maven projects created by `mn` so we can compile with a single command:

![](keyboard.jpg) 
```bash
./gradlew nativeCompile
```
  
Compilation can take a few minutes, but more cores and more memory reduces the required time!

```
> Task :generateResourcesConfigFile
[native-image-plugin] Resources configuration written into /home/rleland/Projects/GitHub/graalvm/graalvm-demos/micronaut-nativeimage/hello/build/native/generated/generateResourcesConfigFile/resource-config.json

> Task :nativeCompile
[native-image-plugin] Toolchain detection is disabled, will use GraalVM from /home/rleland/.local/share/graalvm/graalvm.
[native-image-plugin] Using executable path: /home/rleland/.local/share/graalvm/graalvm/bin/native-image
Warning: Using a deprecated option --allow-incomplete-classpath from 'META-INF/native-image/io.micronaut/micronaut-inject/native-image.properties' in 'file:///home/rleland/.gradle/caches/modules-2/files-2.1/io.micronaut/micronaut-inject/3.5.2/c564af395867f630b24b1b2f382e5a971baf7cad/micronaut-inject-3.5.2.jar'. Allowing an incomplete classpath is now the default. Use --link-at-build-time to report linking errors at image build time for a class or package.
========================================================================================================================
GraalVM Native Image: Generating 'hello' (executable)...
========================================================================================================================
[1/7] Initializing...                                                                                    (2.5s @ 0.32GB)
 Version info: 'GraalVM 22.1.0 Java 11 CE'
 C compiler: gcc (linux, x86_64, 11.2.0)
 Garbage collector: Serial GC
 4 user-provided feature(s)
  - io.micronaut.buffer.netty.NettyFeature
  - io.micronaut.core.graal.ServiceLoaderFeature
  - io.micronaut.http.netty.graal.HttpNettyFeature
  - io.micronaut.jackson.JacksonDatabindFeature
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type kotlin.coroutines.CoroutineContext not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type io.netty.channel.epoll.Epoll not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type org.apache.logging.log4j.core.config.Configurator not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type io.netty.channel.kqueue.KQueue not present
Warning: Could not register complete reflection metadata for io.micronaut.http.bind.binders.ContinuationArgumentBinder. Reason(s): java.lang.TypeNotPresentException: Type kotlin.coroutines.Continuation not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type io.netty.channel.epoll.Epoll not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type kotlinx.coroutines.reactive.ReactiveFlowKt not present
Warning: unable to register annotation value "classes" for annotation type interface io.micronaut.context.annotation.Requires. Reason: java.lang.TypeNotPresentException: Type io.netty.channel.kqueue.KQueue not present
[2/7] Performing analysis...  [***************]                                                         (21.0s @ 4.72GB)
  13,109 (91.99%) of 14,250 classes reachable
  18,655 (59.12%) of 31,555 fields reachable
  65,820 (62.98%) of 104,507 methods reachable
     702 classes,   242 fields, and 2,414 methods registered for reflection
      68 classes,    88 fields, and    55 methods registered for JNI access
[3/7] Building universe...                                                                               (1.9s @ 2.35GB)
[4/7] Parsing methods...      [*]                                                                        (0.9s @ 3.53GB)
[5/7] Inlining methods...     [****]                                                                     (2.1s @ 2.91GB)
[6/7] Compiling methods...    [***]                                                                     (10.1s @ 3.16GB)
[7/7] Creating image...                                                                                  (2.8s @ 4.40GB)
  24.53MB (42.24%) for code area:   43,779 compilation units
  25.56MB (44.01%) for image heap:   9,364 classes and 297,425 objects
   7.99MB (13.75%) for other data
  58.08MB in total
------------------------------------------------------------------------------------------------------------------------
Top 10 packages in code area:                               Top 10 object types in image heap:
   1.60MB sun.security.ssl                                     5.36MB byte[] for code metadata
1006.61KB java.util                                            3.63MB java.lang.Class
 688.43KB com.sun.crypto.provider                              2.52MB java.lang.String
 573.54KB reactor.core.publisher                               2.38MB byte[] for java.lang.String
 560.76KB io.netty.buffer                                      2.37MB byte[] for general heap data
 541.64KB com.oracle.svm.core.reflect                          1.20MB com.oracle.svm.core.hub.DynamicHubCompanion
 497.99KB sun.security.x509                                  615.52KB byte[] for reflection metadata
 479.24KB io.netty.handler.codec.http2                       572.66KB java.lang.String[]
 463.05KB java.util.concurrent                               520.27KB java.util.HashMap$Node
 452.63KB java.lang                                          513.44KB java.lang.reflect.Method
      ... 478 additional packages                                 ... 2913 additional object types
                                           (use GraalVM Dashboard to see all)
------------------------------------------------------------------------------------------------------------------------
                        4.7s (10.5% of total time) in 31 GCs | Peak RSS: 7.70GB | CPU load: 9.25
------------------------------------------------------------------------------------------------------------------------
Produced artifacts:
 /home/rleland/Projects/GitHub/graalvm/graalvm-demos/micronaut-nativeimage/hello/build/native/nativeCompile/hello (executable)
 /home/rleland/Projects/GitHub/graalvm/graalvm-demos/micronaut-nativeimage/hello/build/native/nativeCompile/hello.build_artifacts.txt
========================================================================================================================
Finished generating 'hello' in 44.5s.
[native-image-plugin] Native Image written to: /home/rleland/Projects/GitHub/graalvm/graalvm-demos/micronaut-nativeimage/hello/build/native/nativeCompile

BUILD SUCCESSFUL in 46s
5 actionable tasks: 2 executed, 3 up-to-date

```

The result is a 54M standalone executable placed in the `build/native/nativeCompile` folder named 'hello`.
    
    For Micronaught 3.0 and an older GraalVM distribution this executable was 63M, which is a 14% reduction in size!

![](keyboard.jpg)
```bash
ls -lh build/native/nativeCompile
```

```sh
total 55352
-rwxrwxr-x 1 rleland rleland 56668424 Jun 22 22:14 hello
-rw-rw-r-- 1 rleland rleland       20 Jun 22 22:14 hello.build_artifacts.txt
```

Let's startup the application.  It's a native executable, so we just invoke it:

![](keyboard.jpg) 
```bash
./build/native/nativeCompile/hello
```

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

![](keyboard.jpg)
```bash
docker build . -t hello
```

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

![](keyboard.jpg)
```bash
doker images
```

```sh
REPOSITORY              TAG                 IMAGE ID            CREATED             SIZE
hello                   latest              f5ea290d8d08        2 seconds ago       94.2MB
frolvlad/alpine-glibc   alpine-3.12         d955957758ab        3 months ago        17.9MB
```

We can run the container image and use it just like we did when running the application directly:

![](keyboard.jpg) 
```bash
docker run -p8080:8080 --rm hello
```

![](keyboard.jpg)  
```bash
curl http://localhost:8080/hello
```

```sh
Example Response
```

### Wrapping Up

Micronaut makes it really easy to build modern Java applications and microservices.
Its elimination of runtime reflection also makes it the ideal application framework for use with GraalVM Native Image for ahead of time compilation and containerization.
