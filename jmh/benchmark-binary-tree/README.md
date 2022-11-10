# Binary Tree Benchmark 

This demo shows how to run a Java Microbenchmark Harness (JMH) benchmark as a native executable.

To build a native executable version of this application you need to run the native image tracing agent to supply
the reflection config to the native image builder. This has already been done for you to save time and the generated 
configuration can be found in _src/main/resources/META-INF/native-image/_.

> **NOTE** To generate the configuration yourself, ensure that the JMH `fork` parameter is set to 0,
> which can be performed from the command line using the option  `-f 0`. It can also be achieved within the code by using the `@Fork` annotation.

## Build and Run as a Java Application

To build and then run the benchmark as a Java application, run the following commands:

```shell
./mvnw clean package exec:exec
```

Note that within the _pom.xml_ file there are instructions to explicitly turn off GraalVM JIT compiler using the option `-XX:-UseJVMCICompiler`. 
This means that benchmark will run using the C2 JIT compiler.

The application will run the benchmark and display the results to the terminal. The final result is the most significant. You should see something
like:

```shell
Benchmark          (binaryTreesN)   Mode  Cnt    Score    Error  Units
BinaryTrees.bench              14  thrpt    3  151.088 ± 15.815  ops/s
```

## Build and Run as a Native Executable

Now to build a native executable using Native Image. This demo uses Native Image Enterprise edition; however, if you are using Community
Edition you will probably see lower figures for throughput.

To build the native executable, run the following command:

```shell
./mvnw package -Pnative
```

Then, to run the native executable, run the following command:

```shell
./target/benchmark-binary-tree
```

This is what we saw:

```shell
Benchmark          (binaryTreesN)   Mode  Cnt    Score    Error  Units
BinaryTrees.bench              14  thrpt    3  104.281 ± 71.288  ops/s
```

## Optimise the Native Image - REQUIRES Enterprise Edition

If you are using Native Image Enterprise Edition you can improve the performance of this benchmark by taking 
advantage of a feature of Enterprise Edition, Profile Guided Optimisations (PGO).

**NOTE : This requires Enterprise Edition**

First you will need to build an instrumented version of the native binary that contains extra code to trace the 
execution of the program and to profile it and therefore will run slower than the previous version. When the execution 
has finished it will generate a profile file,`default.iprof` in the root directory. This profile file, containing 
profiling information about the application that can be used to optimise it, will be used to build a more efficient native executable.

To build the instrumented version of the native executable, run the following command:

```shell
./mvnw package -Pinstrumented
```

And then run it to generate the profile file:

```shell
./target/benchmark-binary-tree-instr
```

Now you have generated the profile file, build and run the optimised version of the native executable.

```shell
./mvnw package -Poptimised
./target/benchmark-binary-tree-opt
```

```shell
Benchmark          (binaryTreesN)   Mode  Cnt    Score    Error  Units
BinaryTrees.bench              14  thrpt    3  172.042 ± 77.747  ops/s
```

## Your Mileage May Vary

The results you see will vary depending on the hardware you are running on. The results above are from a 2019 MacBook Pro, i7, 32 GB RAM
running  GraalVM EE 22.2.0 with JDK 17.

Collating the previous data into a chart:

![Binary Tree Benchmark](./images/benchmark-binary-tree.png)