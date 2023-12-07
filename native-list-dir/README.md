# GraalVM Native Image for Faster Startup Demo

This repository contains a Java application that counts files and their sizes in a specified directory.
The demo shows how to compile this Java application ahead-of-time with Oracle GraalVM Native Image and apply Profile-Guided Optimization (PGO) for more performance gains.

### Prerequisites
* [Oracle GraalVM](http://graalvm.org)

## Preparation

1. Download and install the latest GraalVM JDK using [SDKMAN!](https://sdkman.io/).
    ```bash
    sdk install java 21.0.1-graal
    ```

2. Download or clone the repository and navigate into the `native-list-dir` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-list-dir
    ```

## Build the Application

1. Start by compiling the _ListDir.java_ file:
    ```bash
    $JAVA_HOME/bin/javac ListDir.java
    ```

2. Generate a native executable from the class file using [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/): 
    ```bash
    $JAVA_HOME/bin/native-image ListDir
    ```
    
    The `native-image` compiles the application ahead-of-time for faster startup and lower general overhead at run time.
    After executing the `native-image` command, check the directory. It should have produced the executable file _listdir_.

## Run the Application

To run the application, you need to execute the `ListDir` class. 
To make it a little bit more interesting, pass it a directory that actually contains some files, for example `..` (to count files in the parent of the current directory, containing all the demos in this repository).

You can run it as a normal Java application using `java` and check the time spent with the `time` utility. 
```bash
time java ListDir ..
```

Or, since you created a native executable, you can run that directly:
```bash
time ./listdir ..
```

Approximately the following output should be produced (the files count and their sizes will vary of course):

```bash
time java ListDir ..
Walking path: ..
Total: 1106 files, total size = 319110627 bytes
java ListDir ..  0.15s user 0.06s system 147% cpu 0.140 total
```

```bash
time ./listdir ..
Walking path: ..
Total: 1106 files, total size = 319110615 bytes
./listdir ..  0.01s user 0.05s system 88% cpu 0.069 total
```

The performance gain of the native version is largely due to the faster startup.

## Build an Optimized Native Executable with PGO (Oracle GraalVM)

Oracle GraalVM offers extra benefits for building native executables. 
For example, you can apply [Profile-Guided Optimization (PGO)](https://en.wikipedia.org/wiki/Profile-guided_optimization) to improve performance. 
With PGO you can collect the profiling data and then feed it to the `native-image` tool, which will use this information to further optimize the performance of the resulting executable. As an example, a [program demonstrating Java streams](https://github.com/graalvm/graalvm-demos/blob/master/scala-examples/streams/Streams.java) will be used.

1. Build an instrumented image from the _ListDir_ class and run it to collect profiles, specifying a different name for the native executable, for example _listdir-instrumented_:

    ```shell
    $JAVA_HOME/bin/native-image --pgo-instrument ListDir -o listdir-instrumented
    ```
    ```bash
    ./listdir-instrumented ..
    ```
    Profiles collected from this run are now stored in the `default.iprof` file. 
    Note that the profiling now runs with a much smaller data size.

3. Use the profiles gathered at the previous step to build an optimized native executable (specify a different name for the native executable than in the previous run):
    ```shell
    $JAVA_HOME/bin/native-image --pgo ListDir -o listdir-optimized
    ```

4. Run that optimized native executable:
    ```shell
    time ./listdir-optimized ..
    ```

### References

- [Optimize a Native Executable with Profile-Guided Optimizations](https://www.graalvm.org/latest/reference-manual/native-image/guides/optimize-native-executable-with-pgo/)
