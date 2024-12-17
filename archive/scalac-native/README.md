# Native Image of the Scala Compiler

This demo demonstrates how to build a native executable version of the Scala compiler.

## Preparation

1. Set the environment variable `SCALA_HOME` to point to Scala 2.12.x. Check what `scala` you have on your system by running:
    ```bash
    echo $SCALA_HOME
    ```
    ```bash
    scala --version
    ```

2. Download and [install GraalVM](https://www.graalvm.org/downloads/).

3. Download the example application:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/archive/scalac-native
    ```

4. Then enter the `scalac-substitutions` subfolder and  build the project with `sbt`:
    ```bash
    cd scalac-substitutions
    ```
    ```bash
    sbt package
    ```
    ```bash
    cd ..
    ```

5. Build the native image of the Scala compiler:
    ```bash
    ./scalac-image.sh
    ```

    The produced native image is called `scalac` and has no dependencies on the JDK.

    The script `scalac-native` calls the generated compiler and passes all the required parameters (just like `scala` does).

6. Compare to the JVM on the first run:
    ```bash
    time $SCALA_HOME/bin/scalac HelloWorld.scala
    ```
    ```bash
    time ./scalac-native HelloWorld.scala
    ```

When compiled with [Profile-Guided Optimization (PGO)](https://www.graalvm.org/latest/reference-manual/native-image/guides/optimize-native-executable-with-pgo/), the native `scalac` is as fast as the one running on the JVM (with the C2 compiler).

## Support for Macros

For macros to work, the macro classes must be known to the image builder of the Scala compiler.

1. First, try a `scalac` image that includes macro by running:
    ```bash
    ./scalac-native macros/GreetingMacros.scala -d macros/
    ```
    ```bash
    ./scalac-image-macros.sh
    ```

2. Compile a project that uses macros from `GreetingMacros.scala`:
    ```bash
    ./scalac-native -cp macros/ HelloMacros.scala
    ```

3. Run the compiled application with:
  ```bash
  scala HelloMacros
  Hello, World!
  ```

Read more on this topic in [Compiling Scala Faster with GraalVM](https://medium.com/graalvm/compiling-scala-faster-with-graalvm-86c5c0857fa3).