#!/usr/bin/env bash
set -ex

native-image -Ob -m jdk.httpserver -o jwebserver

# Requires GraalVM 25 or later:
cd sbom-test
mvn clean package
mvn -Pnative package