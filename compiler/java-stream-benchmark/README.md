# Java Stream Benchmark

This demo contains the code for a simple Java Stream benchmark designed to run on the [GraalVM JDK](http://graalvm.org) and the [Oracle JDK](https://www.oracle.com/java/technologies/downloads/).
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

### GraalVM JDK with Graal JIT

Run the benchmark with the default GraalVM JIT compiler by executing the _target/benchmarks.jar_ file:
```bash
java -jar target/benchmarks.jar
```

### GraalVM JDK with Graal JIT Deactivated

Now deactivate the Graal compiler and run the benchmark on the same JVM (GraalVM):
```bash
java -XX:-UseJVMCICompiler -jar target/benchmarks.jar
```
This way, the Graal JIT compiler is not be used as the JVMCI compiler and the JVM uses its default one (C2).

### Oracle JDK with Graal JIT

Note that starting with Oracle JDK 23, the **Oracle GraalVM JIT compiler (Graal JIT) is now included among the JITs available as part of the Oracle JDK**.
Read more in [this blog post](https://blogs.oracle.com/java/post/including-the-graal-jit-in-oracle-jdk-23).

1. Switch the JVM from GraalVM (Oracle GraalVM or Community Edition) to Oracle JDK 23 or higher. You can quickly do that with using [SDKMAN!](https://sdkman.io/):
    ```bash
    sdk install java 23.0.1-oracle 
    ```

2. Run this benchmark with the Graal JIT compiler enabled. For that, pass the `-XX:+UseGraalJIT` option to `java`:
    ```bash
    java -XX:+UnlockExperimentalVMOptions -XX:+UseGraalJIT -jar target/benchmarks.jar
    ```

> To switch between JVMs in the same terminal window (without affecting the global setting), use the `sdk` tool. For example, to switch back to Oracle GraalVM for JDK 23, run: `sdk use java 23.0.1-graal`.

Learn more about the Graal JIT compiler from [its official documentation](https://www.graalvm.org/reference-manual/java/compiler/).