#!/bin/bash

# Exit on error
set -e

# JAVA_HOME="/usr/lib/jvm/java-11-graalvm/"
# JDK8_HOME="/usr/lib/jvm/java-8-openjdk"

if [[ -z "$JAVA_HOME" ]]; then
    echo "JAVA_HOME is not set"
    exit 1
fi

if [[ -z "$JDK8_HOME" ]]; then
    echo "JDK8_HOME is not set"
    exit 1
fi

JDK8_JRE="$JDK8_HOME"
if [ -d "$JDK8_JRE/jre" ]; then
  JDK8_JRE="$JDK8_JRE/jre"
fi

JAVAC_BOOT_CLASSPATH="$JDK8_JRE/lib/resources.jar:$JDK8_JRE/lib/rt.jar:$JDK8_JRE/lib/sunrsasign.jar:$JDK8_JRE/lib/jsse.jar:$JDK8_JRE/lib/jce.jar:$JDK8_JRE/lib/charsets.jar:$JDK8_JRE/lib/jfr.jar:$JDK8_JRE/classes"

./espresso-jshell \
    -Dorg.graalvm.home="$JAVA_HOME" \
    -Rjava.JavaHome="$JDK8_HOME" \
    -Rjava.BootClasspathAppend="./jshell8.jar" \
    -C-source -C8 \
    -C-target -C8 \
    -C-bootclasspath -C"$JAVAC_BOOT_CLASSPATH" \
    "$@"
