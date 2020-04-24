# GraalVM demos: Simple Java Stream Benchmark

This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [Maven](https://maven.apache.org/)
* [GraalVM](http://graalvm.org)

## Preparation

1. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$GRAALVM_HOME` and add `$GRAALVM_HOME/bin` to the `PATH` environment variable.
On Linux:
```
export GRAALVM_HOME=/home/${current_user}/path/to/graalvm
export PATH=$GRAALVM_HOME/bin:$PATH
```
On macOS:
```
export GRAALVM_HOME=/Users/${current_user}/path/to/graalvm/Contents/Home
export PATH=$GRAALVM_HOME/bin:$PATH
```

2. Download or clone the repository and navigate into the `java-simple-stream-benchmark` directory:

```
git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos/java-simple-stream-benchmark
```

3. Build the benchmark. You can manually execute `mvn package`, but there is also a `build.sh` script included for your convenience.
```
./build.sh
```

Now you are all set to execute the benchmark and compare the results between different JVMs.

## Running the Benchmark

To run the benchmark, you need to execute the `target/benchmarks.jar` file. You can run it with the following command:

```
$GRAALVM_HOME/bin/java -jar target/benchmarks.jar
```
If you would like to run the benchmark on a different JVM, you can run it with
whatever `java` you have. However, if you want to run it on the same JVM, just
without the Graal compiler, you can simply add the `-XX:-UseJVMCICompiler`
command line option into the same command.

```
$GRAALVM_HOME/bin/java -XX:-UseJVMCICompiler -jar target/benchmarks.jar
```

This way, Graal will not be used as the JVMCI compiler and the JVM will use its default one.

## A Note About the Results

The benchmark mode is the `AverageTime` in nanoseconds per operation, which
means lower numbers are better. Note that the results you see can be influenced
by the hardware you are running this benchmark on, the CPU load, and other
factors. Interpret them responsibly.
