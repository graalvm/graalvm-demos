#!/usr/bin/env bash
set -ex

# Run the application on the JVM
./gradlew clean run
# Build and run a native executable (`nativeRun` compiles the application, invokes `nativeCompile`, and then runs the executable)
./gradlew nativeRun