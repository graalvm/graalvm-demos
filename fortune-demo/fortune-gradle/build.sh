#!/bin/bash

# Exit on error
set -e

# Run the application with the agent on JVM
./gradlew -Pagent run
# Copy metadata into /META-INF/native-image directory
./gradlew metadataCopy --task run --dir src/main/resources/META-INF/native-image
# Build a native executable using configuration (`nativeRun` compiles the app, then invokes `nativeCompile`, and then runs the native binary)
./gradlew nativeRun
# Run JUnit tests
./gradlew nativeTest
# Run tests on JVM with the agent
./gradlew -Pagent test
# Test building a native executable using metadata
./gradlew -Pagent nativeTest