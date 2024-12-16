# Java Stream Benchmark

This repository contains the code for a simple Java Stream benchmark designed to run on a JDK.

## Preparation

1. Download and install the GraalVM JDK using [SDKMAN!](https://sdkman.io/). For other installation options, visit the [Downloads page](https://www.graalvm.org/downloads/).
    ```bash
    sdk install java 23.0.1-graal
    ```

2. Download or clone the repository and navigate into the benchmark directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/compiler/java-stream-benchmark
    ```

## Build the Benchmark

Build the benchmark with Maven:
```bash
./mvnw package
```
Now you can run this benchmark with whatever `java` you have on your machine and compare the results between JVMs.

> **A note about the results**: The benchmark mode is the `AverageTime` in nanoseconds per operation, which means lower numbers are better.
Note that the results you see can be influenced by the hardware you are running this benchmark on, the CPU load, and other factors.
Interpret them responsibly.

## Run the Benchmark

1. Run the benchmark on the **GraalVM JDK with the default JIT compiler (Graal)** by executing the _target/benchmarks.jar_ file:
    ```bash
    java -jar target/benchmarks.jar
    ```

2. Now **deactivate the Graal compiler** using the `-XX:-UseJVMCICompiler` option and run the benchmark on the same JVM (GraalVM):
    ```bash
    java -XX:-UseJVMCICompiler -jar target/benchmarks.jar
    ```
    This way, the Graal JIT compiler is not be used as the JVMCI compiler and the JVM uses its default one.

## Learn More

Learn more about the Graal JIT compiler from [its official documentation](https://www.graalvm.org/reference-manual/java/compiler/).

Note that starting with Oracle JDK 23, the **Oracle GraalVM JIT compiler (Graal JIT) is now included among the JITs available as part of the Oracle JDK**. 
To run a Java application on Oracle JDK with the Graal JIT compiler, pass the `-XX:+UseGraalJIT` option to `java`. 
Read more in [this blog post](https://blogs.oracle.com/java/post/including-the-graal-jit-in-oracle-jdk-23).