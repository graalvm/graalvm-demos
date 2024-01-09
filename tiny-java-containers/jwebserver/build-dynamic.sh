#!/bin/sh

# compile with fully dynamically linked shared libraries
native-image -Ob -m jdk.httpserver -o jwebserver.dynamic

# Distroless Java Base-provides glibc and other libraries needed by the JDK
docker build . -f Dockerfile.distroless-java-base.dynamic -t jwebserver:distroless-java-base.dynamic

