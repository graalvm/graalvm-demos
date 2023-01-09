# Application Initialization at Build Time

This example demonstrates the ability of `native-image` to run parts of your application at the image build time.

In both examples we use the Jackson framework to parse a JSON file to determine which `Handler` should be used by the application (`CurrentTimeHandler` or `HelloWorldHandler`) at runtime.

* In `configure-at-runtime-example` the JSON parsing happens at image run time
  and thus contributes to the image startup time. In addition, all methods and
  static fields originated from the Jackson framework that are needed become part
  of a native executable.

* In contrast, `configure-at-buildtime-example` performs the JSON parsing as
  part of building the native executable. In this case the executable does not contain any parts
  of the Jackson framework (can be verified easily by adding
  `-H:+PrintAnalysisCallTree` to the `<buildArgs>` in `pom.xml`).  When this
  image gets executed, it can run the handler right away since it was already
  determined at build time which hander should be used.

## Preparation

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) 
    ```

2. Download or clone the repository and navigate into the `native-image-configure-examples` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-image-configure-examples
    ```

## Build and run examples

1. Change to one of the demo subdirectories, for example, `configure-at-runtime-example`:
    ```bash
    cd graalvm-demos/native-image-configure-examples/configure-at-runtime-example
    ```

2. Build the project:
    ```bash
    mvn package
    ```
3. Once the Maven build succeeds, a native executable called "example" will be generated in the `configure-at-runtime-example/target/` directory. Run it:
    ```bash
    ./target/example
    Tue Mar 23 22:17:33 EET 2021
    ```
4. Repeat the same steps for the other sub-demo:
    ```bash
    cd ..
    cd configure-at-buildtime-example
    ```
    ```bash
    mvn package
    ```
    ```bash
    ./target/example
    ```
    You will see the next output:
    ```
    Hello, world!
    ```
5. Finally, you may shutdown the `native-image` server:
    ```bash
    $JAVA_HOME/bin/native-image --server-shutdown
    ```

Loading application configuration at executable build time can speed up application startup. See [Build-Time Initialization](https://www.graalvm.org/dev/reference-manual/native-image/optimizations-and-performance/ClassInitialization/) to learn more. 

To read more about the topic of class initialization, see [Initialize Once, Start Fast: Application Initialization at Build Time](http://www.christianwimmer.at/Publications/Wimmer19a/Wimmer19a.pdf).
