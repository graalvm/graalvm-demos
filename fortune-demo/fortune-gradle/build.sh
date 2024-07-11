#!/bin/bash

# Exit on error
set -e

# Run the application with the agent on JVM
./gradlew -Pagent run
# Build a native executable using configuration (`nativeRun` compiles the app, then invokes `nativeCompile`, and then runs the native binary)
./gradlew nativeCompile
# Run the application
./fortune/build/native/nativeCompile/fortune