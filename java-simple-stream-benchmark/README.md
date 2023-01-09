# Simple Java Stream Benchmark

This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
- [GraalVM](http://graalvm.org)

## Preparation

1. Download and install the latest GraalVM JDK using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader). 
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) 
    ```

2. Download or clone the repository and navigate into the `java-simple-stream-benchmark` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/java-simple-stream-benchmark
    ```

## Build and Run the Benchmark

1. Build the benchmark. You can manually execute `mvn package`, but there is also a `build.sh` script included for your convenience.
    ```bash
    ./build.sh
    ```
    Now you are all set to execute the benchmark and compare the results between different JVMs.

2. To run the benchmark, you need to execute the `target/benchmarks.jar` file. You can run it with the following command:
    ```bash
    $JAVA_HOME/bin/java -jar target/benchmarks.jar
    ```

If you would like to run the benchmark on a different JVM, you can run it with whatever `java` you have.
However, if you want to run it on the same JVM, just without the GraalVM compiler, you can simply add the `-XX:-UseJVMCICompiler`
command line option into the same command.
```bash
$JAVA_HOME/bin/java -XX:-UseJVMCICompiler -jar target/benchmarks.jar
```

This way, the GraalVM compiler will not be used as the JVMCI compiler and the JVM will use its default one.

## A note about the results

The benchmark mode is the `AverageTime` in nanoseconds per operation, which means lower numbers are better.
Note that the results you see can be influenced by the hardware you are running this benchmark on, the CPU load, and other factors.
Interpret them responsibly.
