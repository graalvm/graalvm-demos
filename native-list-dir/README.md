# GraalVM Demos: Native images for Faster Startup

This repository contains the code for a demo application for [GraalVM](http://graalvm.org).

## Prerequisites
* [GraalVM](http://graalvm.org)
* [Native Image](https://www.graalvm.org/latest/reference-manual/native-image/)

## Preparation

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk)
    ```

2. Download or clone the repository and navigate into the `native-list-dir` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-list-dir
    ```

## Building the Application

There are two Java classes. Start by building _ListDir.java_ for the purposes of this demo. You can manually execute `javac ListDir.java`, but there is also a `build.sh` script included for your convenience.

>Note: You can use any JDK for compiling the Java classes, but in the build script GraalVM's javac is used to simplify the prerequisites and not depend on you having another JDK installed.

Run building script:
```bash
./build.sh
```

The important line of the `build.sh` creates a native image from our Java class. Have a look at it:
```bash
$JAVA_HOME/bin/native-image ListDir
```

The `native-image` compiles the application ahead-of-time for faster startup and lower general overhead at runtime.
After executing the `native-image` command, check the directory. It should have produced an executable file `listdir`.

## Running the Application

To run the application, you need to execute the `ListDir` class. You can run it as a normal Java application using `java`. Or, since we have a native image prepared, you can run that directly. The `run.sh` file, executes both, and times them with the `time` utility.
```bash
time java ListDir $1
```

To make it a little bit more interesting, pass it a directory that actually contains some file: `./run.sh ..` (`..` - is the parent of the current directory, the one containing all the demos in this repository).

Approximately the following output should be produced (the files count and their sizes will vary of course):
```
→ ./run.sh ../..
+ java ListDir ../..
Walking path: ../..
Total: 26233 files, total size = 556731978 bytes

real	0m1.715s
user	0m3.613s
sys	0m1.133s
+ ./listDir ../..
Walking path: ../..
Total: 26233 files, total size = 556731978 bytes

real	0m0.883s
user	0m0.203s
sys	0m0.657s
```

The performance gain of the native version is largely due to the faster startup.

## Polyglot Capabilities

You can also experiment with a more sophisticated `ExtListDir` example, which takes advantage of GraalVM's Java and JavaScript polyglot capabilities.

1. Compile the source Java code:
    ```shell
    $JAVA_HOME/bin/javac ExtListDir.java
    ```

2. Build a native image. Building the native executable command is similar to the one above, but since the example uses JavaScript, you need to inform the `native-image` utility about that by passing the `--language:js` option. Note that it takes a bit more time because it needs to include the JavaScript support.
    ```shell
    $JAVA_HOME/bin/native-image --language:js ExtListDir
    ```

3. Execute it. The execution is the same as in the previous example:
    ```bash
    time java ExtListDir $1
    ```
    ```bash
    time ./extlistdir $1
    ```

## Profile-Guided Optimizations for High Throughput

Oracle GraalVM Enterprise Edition offers extra benefits for building native executables.
With GraalVM Enterprise Edition, you can apply Profile-Guided Optimization (PGO) to improve performance.
[Profile-Guided Optimization (PGO) ](https://en.wikipedia.org/wiki/Profile-guided_optimization) is a commonly used technique in the Java ecosystem to mitigate the missing just-in-time optimization and gather the execution profiles at one run and then use them to optimize subsequent compilation(s). With PGO you can collect the profiling data and then feed it to the `native-image` tool, which will use this information to further optimize the performance of the resulting executable. As an example, a [program demonstrating Java streams](https://github.com/graalvm/graalvm-demos/blob/master/scala-examples/streams/Streams.java) will be used.

As an example, a [program demonstrating Java streams](https://github.com/graalvm/graalvm-demos/blob/master/scala-examples/streams/Streams.java) will be used.

1. Run the application with `java` to see the output:
    ```bash
    $JAVA_HOME/bin/javac Streams.java
    ```
    ```bash
    $JAVA_HOME/bin/native-image Streams
    ```
    ```bash
    ./streams 1000000 200
    ```
    ```
    Iteration 20 finished in 1955 milliseconds with checksum 6e36c560485cdc01
    ```

2. Build an instrumented image and run it to collect profiles:
    ```shell
    $JAVA_HOME/bin/native-image --pgo-instrument Streams
    ```
    ```bash
    ./streams 1000 200
    ```
    Profiles collected from this run are now stored in the `default.iprof` file. Note that the profiling now runs with a much smaller data size.

3. Use the profiles gathered at the previous step to build an optimized native executable:
    ```shell
    $JAVA_HOME/bin/native-image --pgo Streams
    ```

4. Run that optimized native executable:
    ```shell
    ./streams 1000000 200
    ```
    ```
    Iteration 20 finished in 827 milliseconds with checksum 6e36c560485cdc01
    ```
    You should see more than 2x improvements in performance.
