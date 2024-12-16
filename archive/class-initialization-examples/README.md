# Application Initialization at Build Time

This example demonstrates the ability of `native-image` to run parts of your application at the build time.

In both examples we use the Jackson framework to parse a JSON file to determine which `Handler` should be used by the application (`CurrentTimeHandler` or `HelloWorldHandler`) at runtime.

* In `configure-at-runtime-example` the JSON parsing happens at image run time
  and thus contributes to the image startup time. In addition, all methods and
  static fields originated from the Jackson framework that are needed become part
  of a native executable.

* In contrast, `configure-at-buildtime-example` performs the JSON parsing as
  part of building the native executable. In this case the executable does not contain any parts
  of the Jackson framework (can be verified easily by adding
  `-H:+PrintAnalysisCallTree` to the `<buildArgs>` in `pom.xml`). When this
  image gets executed, it can run right away since it was already
  determined at build time which handler should be used.

## Preparation

1. Download and install the latest GraalVM JDK using [SDKMAN!](https://sdkman.io/).
    ```bash
    sdk install java 21.0.5-graal
    ```

2. Download or clone the repository and navigate into the `class-initialization-examples` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/archive/class-initialization-examples
    ```

## Build and Run Examples

1. Change to one of the demo subdirectories, for example, `configure-at-runtime-example`:
    ```bash
    cd configure-at-runtime-example
    ```
2. Build the project:
    ```bash
    mvn package
    ```
3. Once the Maven build succeeds, a native executable called "runtime-example" will be generated in the _configure-at-runtime-example/target/_ directory. Run it:
    ```bash
    ./target/runtime-example
    ```
    You will see the current date and time message.
4. Repeat the same steps for the other sub-demo:
    ```bash
    cd ../configure-at-buildtime-example
    ```
    ```bash
    mvn package
    ```
    ```bash
    ./target/buildtime-example
    ```
    You will see the next output:
    ```
    Hello, world!
    ```

Loading application configuration at executable build time can speed up application startup.

Learn more about [Class Initialization in Native Image](https://www.graalvm.org/latest/reference-manual/native-image/optimizations-and-performance/ClassInitialization/) at the website, and from the blog post [Understanding Class Initialization in GraalVM Native Image Generation](https://medium.com/graalvm/understanding-class-initialization-in-graalvm-native-image-generation-d765b7e4d6ed).