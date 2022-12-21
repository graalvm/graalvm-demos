# Building Statically Linked or Mostly-Statically Linked Native Executables.

GraalVM Native Image by default builds dynamically linked binaries: at build time it loads your application classes and interfaces and hooks them together in a process of dynamic linking.

However, you can create a statically linked or mostly-static linked native executable, depending on your needs. 

**A static native executable** is a statically linked binary that can be used without any additional library dependencies.
A static native executable is easy to distribute and deploy on a slim or distroless container (a scratch container).
You can create a static native executable by statically linking it against [musl-libc](https://musl.libc.org/), a lightweight, fast and simple `libc` implementation.

**A mostly-static native executable** is a binary that links everything (`zlib`, JDK shared libraries) except the standard C library, `libc`. This is an alternative option to staticly linking everything. Also, depending on the user's code, it may link `libstdc+` and `libgcc`.
This approach is ideal for deployment on a distroless container image.

> Note: This currently only works when linked against `libc`.

This guide shows how to build a fully static and mostly-static native executable.

### Prerequisites

- Linux AMD64 operating system
- GraalVM distribution for Java 11 or higher.
- A 64-bit `musl` toolchain, `make`, and `configure`
- The latest `zlib` library

## Preparation

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) -c 'native-image' 
    ```

    To meet the prerequisites, you need to install the `musl` toolchain, compile and install `zlib` into the toolchain.

2. Download the `musl` toolchain from [musl.cc](https://musl.cc/). (We recommend [this one](https://more.musl.cc/10/x86_64-linux-musl/x86_64-linux-musl-native.tgz). Extract the toolchain to a directory of your choice. This directory will be referred as `$TOOLCHAIN_DIR`. 

    Download the latest `zlib` library sources from [zlib.net](https://zlib.net/) and extract them. (This documentation uses `zlib-1.2.11`.)
    Create a new environment variable, named `CC`:
    ```bash
    CC=$TOOLCHAIN_DIR/bin/gcc
    ```
    Change into the `zlib` directory, and then run the following commands to compile and install `zlib` into the toolchain:
    ```bash
    ./configure --prefix=$TOOLCHAIN_DIR --static
    ```
    ```bash
    make
    ```
    ```bash
    make install
    ```

3. Download or clone the repository and navigate into the `native-static-images` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-static-images
    ```

## Build a Static Native Executable

The application, _EnvMap.java_, iterates over your environment variables and prints out the ones that contain the `String` of characters passed as a command line argument.

1. First, ensure the directory named `$TOOLCHAIN_DIR/bin` is present on your `PATH`.
    To verify this, run the following command:
    ```bash
    x86_64-linux-musl-gcc
    ```
    
    You should see output similar to the following:
    ```
    x86_64-linux-musl-gcc: fatal error: no input files
    compilation terminated.
    ```

2. Compile the application:
    ```shell
    $JAVA_HOME/bin/javac EnvMap.java
    ```

3. Build a fully statically linked native executable by running this command:
    ```shell
    $JAVA_HOME/bin/native-image --static --libc=musl EnvMap
    ```
    This produces a native executable with statically linked system libraries (including JDK-shared libraries).
    You can pass other arguments before a class or JAR file.

One way to check what dynamic libraries your application depends on is to run `ldd` with the native executable, for example, `ldd helloworld`.

## Build a Mostly-Static Native Executable

With GraalVM Native Image you can build a mostly-static native executable that statically links everything except `libc`. Statically linking all your libraries except `libc` ensures your application has all the libraries it needs to run on any Linux `libc`-based distribution.

To build a  a mostly-static native executable for the above _EnvMap.java_ demo, run:

    ```shell
    $JAVA_HOME/bin/native-image -H:+StaticExecutableWithDynamicLibC EnvMap
    ```

This produces a native executable that statically links all involved libraries (including JDK-shared libraries) except for `libc`. This includes `zlib`. Also, depending on the user's code, it may link `libstdc+` and `libgcc`.

### Frequently Asked Questions

#### What is the recommended base Docker image for deploying a static or mostly-static native executable?

A fully static native executable gives you the most flexibility to choose a base container image - it can run on anything including a `FROM scratch` image.
A mostly-static native executable requires a container image that provides `libc`, but has no additional requirements.
In both cases, choosing the base container image generally depends on your native executable's specific requirements.

### Learn More

* [Tiny Java Containers](https://github.com/graalvm/graalvm-demos/tree/master/tiny-java-containers) demo shows how a simple Java application and a simple web server can be compiled to produce very small Docker container images using various lightweight base images.
* [GraalVM Native Image, Spring and Containerisation](https://luna.oracle.com/lab/fdfd090d-e52c-4481-a8de-dccecdca7d68) interactive lab to build a mostly static executable of a Spring Boot application.