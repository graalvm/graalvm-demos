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

To learn more about this topic, read [Initialize Once, Start Fast: Application Initializationat Build Time](http://www.christianwimmer.at/Publications/Wimmer19a/Wimmer19a.pdf).

## Preparation

1. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$JAVA_HOME` and add `$JAVA_HOME/bin` to the `PATH` environment variable.

  On Linux:
  ```bash
  export JAVA_HOME=/home/${current_user}/path/to/graalvm
  export PATH=$JAVA_HOME/bin:$PATH
  ```
  On macOS:
  ```bash
  export JAVA_HOME=/Users/${current_user}/path/to/graalvm/Contents/Home
  export PATH=$JAVA_HOME/bin:$PATH
  ```
  On Windows:
  ```bash
  setx /M JAVA_HOME "C:\Progra~1\Java\<graalvm>"
  setx /M PATH "C:\Progra~1\Java\<graalvm>\bin;%PATH%"
  ```

2. Install [Native Image](https://www.graalvm.org/dev/reference-manual/native-image/#install-native-image) by running:
  ```bash
  gu install native-image
  ```
  You will also need to provide some native compiler toolchain prerequisites.

3. Download or clone the demos repository::
  ```
  git clone https://github.com/graalvm/graalvm-demos
  ```

## Build and run examples

1. Change to one of the demo subdirectories, for example, _configure-at-runtime-example_:
  ```
  cd graalvm-demos/native-image-configure-examples/configure-at-runtime-example
  ```
2. Build the project:
  ```bash
  mvn package
  ```
3. Once the Maven build succeeds, a native executable called "example" will be generated in the _configure-at-runtime-example/target/_ folder. Execute it:
  ```
  ./target/example
  Tue Mar 23 22:17:33 EET 2021
  ```
4. Repeat the same steps for the other sub-demo:
  ```
  cd ..
  cd configure-at-buildtime-example
  mvn package
  ./target/example
  Hello, world!
  ```
5. Finally, you may shutdown the `native-image` server:
  ```
  $JAVA_HOME/bin/native-image --server-shutdown
  ```
