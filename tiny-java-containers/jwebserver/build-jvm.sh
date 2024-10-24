#!/bin/sh

# Debian Slim image with JDK
docker build . -f Dockerfile.jvm-debian-slim -t jwebserver:debian

# Eclipse Temuring JDK Image
docker build . -f Dockerfile.jvm-eclipse-temurin -t jwebserver:temurin

# Distoless Java 21 (Debian)
docker build . -f Dockerfile.jvm-distroless-java -t jwebserver:distroless-java
