# Binary Tree Benchmark 

This demo shows how to run a Java Microbenchmark Harness (JMH) benchmark as a native executable.

To build a native executable version of this benchmark you need to run the [Tracing Agent](https://www.graalvm.org/latest/reference-manual/native-image/metadata/AutomaticMetadataCollection/) to supply the reflection configuration to the `native-image` builder.
This has already been done for you to save time and the generated configuration can be found in _src/main/resources/META-INF/native-image/_.

> **Note:** To generate the configuration yourself, ensure that the JMH `fork` parameter is set to `0`, which can be performed from the command line using the option  `-f 0`. It can also be achieved within the code by using the `@Fork` annotation.

## Important Notes on Using JMH with GraalVM Native Image

To make a JMH benchmark run as a native image, you can not fork the benchmark process in the same way as JMH does when running on the JVM.
When running on HotSpot, JMH will fork a new JVM for each benchmark to ensure there is no interference in the measurements for each benchmark.
This forking process is not possible with GraalVM Native Image and you should consider the following guidance when building JMH benchmarks that are meant to be run as native executables:
* You should only include a single benchmark in each native executable.
* You need to annotate the benchmark with `@Fork(0)` to ensure that the benchmark is not forked.
* If you want to profile the benchmark to generate an optimized version of it, you should, obviously, ignore the benchmark results whilst profiling.

## Preparation

1. Download and install the GraalVM JDK using [SDKMAN!](https://sdkman.io/). For other installation options, visit the [Downloads page](https://www.graalvm.org/downloads/).
    ```bash
    sdk install java 21.0.5-graal
    ```

2. Download or clone the repository and navigate into the _/native-image/benchmark/jmh/binary-tree_ directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-image/benchmark/jmh/binary-tree
    ```

## Build and Run the Benchmark on HotSpot

To build and run the benchmark on HotSpot, run the following Maven command:
```shell
./mvnw clean package exec:exec
```

Note that within the _pom.xml_ file there are instructions to explicitly turn off the Graal JIT compiler using the option `-XX:-UseJVMCICompiler`. 
This means that benchmark will run using the C2 JIT compiler.

The application runs the benchmark and displays the results to the terminal.
**The final result is the most significant.**
You should see the result similar to this:
```shell
Benchmark          (binaryTreesN)   Mode  Cnt    Score   Error  Units
BinaryTrees.bench              14  thrpt    6  348.923 ± 21.343  ops/s
```

## Build and Run the Benchmark from a Native Executable

Now build a native executable using Native Image.
This demo uses Oracle GraalVM Native Image, however, if you are using GraalVM Community, you may see lower figures for throughput.

1. Build a native executable, run the following command:
    ```shell
    ./mvnw package -Pnative
    ```
2. Run the benchmark from a native executable: 
    ```shell
    ./target/benchmark-binary-tree
    ```
    You should see similar results:
    ```shell
    Benchmark          (binaryTreesN)   Mode  Cnt    Score   Error  Units
    BinaryTrees.bench              14  thrpt    6  282.335 ± 5.644  ops/s
    ```

## Optimize the Benchmark for Throughput

You can improve the performance of this benchmark by applying [Profile-Guided Optimization (PGO)](https://www.graalvm.org/reference-manual/native-image/optimizations-and-performance/PGO/). 

> PGO is available with Oracle GraalVM Native Image.

First, you will need to build an instrumented version of this native benchmark that contains extra code to trace the execution of the program and to profile it.
Therefore, it will run slower than the previous version. 
After execution finishes, a profile file, _default.iprof_, is generated in the root directory.
This file contains profiling information about the application and will be used to build a more efficient native executable.

1. To build the instrumented version of the native executable, run the following command:
    ```shell
    ./mvnw package -Pinstrumented
    ```

2. Then run it to generate the profile file:
    ```shell
    ./target/benchmark-binary-tree-instr
    ```

3. Now that you have the profiles, build and run the optimized version of this native benchmark:
    ```shell
    ./mvnw package -Poptimised
    ```
    ```shell
    ./target/benchmark-binary-tree-opt
    ```
    You should see similar results:
    ```shell
    Benchmark          (binaryTreesN)   Mode  Cnt    Score   Error  Units
    BinaryTrees.bench              14  thrpt    6  311.630 ± 3.630  ops/s
    ```

## Your Mileage May Vary

The results you see will vary depending on the hardware you are running on.
The results above are from a 2019 MacBook Pro, i7, 32 GB RAM running on Oracle GraalVM for JDK 21.0.5.