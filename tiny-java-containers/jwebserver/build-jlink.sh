#!/bin/sh

# Distroless Java Base-provides glibc and other libraries needed by the JDK
docker build . -f Dockerfile.distroless-java-base.jlink -t jwebserver:distroless-java-base.jlink
