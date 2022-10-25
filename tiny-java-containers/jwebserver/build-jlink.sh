#!/bin/sh

rm -rf jwebserver-jlink
jlink \
        --module-path ${JAVA_HOME}/jmods \
        --add-modules jdk.httpserver \
        --verbose \
        --strip-debug \
        --compress 2 \
        --no-header-files \
        --no-man-pages \
        --strip-java-debug-attributes \
        --output jwebserver-jlink

# Distroless Java Base-provides glibc and other libraries needed by the JDK
docker build . -f Dockerfile.distroless-java-base.jlink -t jwebserver:distroless-java-base.jlink
