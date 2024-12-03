#!/usr/bin/env bash
set -ex

./mvnw clean package
./mvnw native:compile -Pnative

# Linux
docker build -f Dockerfiles/Dockerfile.native --build-arg APP_FILE=benchmark-jibber -t jibber-benchmark:native.0.0.1-SNAPSHOT .

# Multistage build for MacOS, Windows, and Linux
docker build -f Dockerfiles/Dockerfile -t jibber-benchmark:native.0.0.1-SNAPSHOT .