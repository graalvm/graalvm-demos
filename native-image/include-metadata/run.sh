#!/usr/bin/env bash
set -ex

## Run Maven Project
# Default Configuration
./mvnw -DskipTests clean package
./mvnw -DskipTests -Pnative package
./target/h2example

# With Tracing Agent
./mvnw clean
./mvnw -Pnative -DskipTests -DskipNativeBuild=true package exec:exec@java-agent
./mvnw -Pnative -DskipTests package exec:exec@native

## Run Gradle Project
# Default Configuration
./gradlew run
./gradlew nativeRun
./gradlew clean

# With Tracing Agent
./gradlew -Pagent run
./gradlew metadataCopy --task run --dir src/main/resources/META-INF/native-image
./gradlew nativeRun
./gradlew clean