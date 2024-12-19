#!/usr/bin/env bash
set -ex

native-image -Ob --enable-sbom=cyclonedx -m jdk.httpserver -o jwebserver

# Requires GraalVM for JDK 24 Early Access build 24.ea.23-graal or later:
# sdk install java 24.ea.23-graal
cd sbom-test
mvn clean package
mvn -Pnative package