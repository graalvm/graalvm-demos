#!/bin/bash

# Exit on error
set -e

# Compile the project and build a native executable
./gradlew nativeRun
# Run the application with the agent on JVM
./gradlew -Pagent run
# Copy metadata into /META-INF/native-image directory
./gradlew metadataCopy --task run --dir src/main/resources/META-INF/native-image
# Build a native executable using configuration
./gradlew nativeCompile
# Run the application from a native executable
./fortune/build/native/nativeCompile/fortune