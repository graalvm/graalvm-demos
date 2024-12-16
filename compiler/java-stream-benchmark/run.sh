#!/usr/bin/env bash
set -xe

# Build
./mvnw package

# Run on GraalVM JDK with Graal JIT
java -jar target/benchmarks.jar

# Run on GraalVM JDK with C2
java -XX:-UseJVMCICompiler -jar target/benchmarks.jar