# GraalVM demos: Simple Java stream benchmark

This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [Maven](https://maven.apache.org/)
* [GraalVM](http://graalvm.org)

## Preparation

Download or clone the repository and navigate into the `java-simple-stream-benchmark` directory:

```
git clone https://github.com/shelajev/graalvm-demos
cd graalvm-demos/java-simple-stream-benchmark
```

Build the benchmark. You can manually execute `mvn package`, but there's also a `build.sh` script included for your convenience.

Execute:
```
./build.sh
```

Now you're all set to execute the benchmark and compare the results between different JVMs.

Export the GraalVM home directory as the `$GRAALVM_HOME` and add `$GRAALVM_HOME/bin` to the path. Here's what I have in my `~/.bashrc` on my MacBook, note that your paths are likely to be different depending on the download location.

```
GRAALVM_VERSION=0.32
export GRAALVM_HOME=/Users/${current_user}/repo/graal-releases/graalvm-$GRAALVM_VERSION/Contents/Home
```

## Running the benchmark

To run the benchmark, you need to execute the `target/benchmarks.jar` file. You can run it with the following command:

```
$GRAALVM_HOME/bin/java -jar target/benchmarks.jar
```
If you'd like to run the benchmark on a different JVM, you can run it with whatever `java` you have. However, if you just want to run it on the same JVM, just without the Graal compiler you can just add the `-XX:-UseJVMCICompiler` command line option into the same command.

```
$GRAALVM_HOME/bin/java -XX:-UseJVMCICompiler -jar target/benchmarks.jar
```

This way, Graal won't be used as the JVMCI compiler and the JVM will use it's default one.

## A note about the results

The benchmark mode is the `AverageTime` in nanoseconds per operation, which means lower numbers are better.

Note that the results you see can be influenced by the hardware you're running this benchmark on, the CPU load, and other factors. Interpret them responsibly.
