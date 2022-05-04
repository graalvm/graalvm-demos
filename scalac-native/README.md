# Native Image of the Scala Compiler

This demo demonstrates how to build a native image of the Scala compiler.

1. First, make sure that the environment variables `SCALA_HOME` points to Scala 2.12.x. Check what `scala` you have on your system by running:
  ```bash
  echo $SCALA_HOME
  scala --version
  ```

2. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$JAVA_HOME` and add `$JAVA_HOME/bin` to the `PATH` environment variable.

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

3. Download or clone the repository and navigate into the `scala-examples/scalac-native` directory:
  ```bash
  git clone https://github.com/graalvm/graalvm-demos
  cd graalvm-demos/scala-examples/scalac-native
  ```

4. Then enter the `scalac-substitutions` subfolder and  build the project with `sbt`:
  ```bash
  cd scalac-substitutions
  sbt package
  cd ..
  ```

5. Build the native image of the Scala compiler:
  ```bash
  ./scalac-image.sh
  ```

The produced native image is called `scalac` and has no dependencies on the JDK.

The script `scalac-native` calls the generated compiler and passes all the required parameters (just like `scala` does).

Compare to the JVM on the first run:
```bash
time $SCALA_HOME/bin/scalac HelloWorld.scala

real	0m2.315s
user	0m5.868s
sys	0m0.248s

time ./scalac-native HelloWorld.scala

real	0m0.177s
user	0m0.129s
sys	0m0.034s
```

When compiled with [Profile-Guided Optimization (PGO)](https://www.graalvm.org/reference-manual/native-image/PGO/) the native `scalac` is as fast as the one running on the JVM (with the C2 compiler).

## Support for Macros

For macros to work, the macro classes must be known to the image builder of the Scala compiler.

1. First, try a `scalac` image that includes macro, run:
  ```bash
  ./scalac-native macros/GreetingMacros.scala -d macros/
  ./scalac-image-macros.sh
  ```

2. Now you can compile a project that uses macros from `GreetingMacros.scala`:
  ```bash
  ./scalac-native -cp macros/ HelloMacros.scala
  ```

3. Run the compiled program with:
  ```bash
  scala HelloMacros
  Hello, World!
  ```
