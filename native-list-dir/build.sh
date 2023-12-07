#!/usr/bin/env bash
set -ex

# Build a native excutable
$JAVA_HOME/bin/javac ListDir.java
$JAVA_HOME/bin/native-image ListDir
time java ListDir $1
time ./listdir $1

# Build an optimized native excutable with PGO
$JAVA_HOME/bin/native-image --pgo-instrument ListDir -o listdir-instrumented
./listdir-instrumented $1
$JAVA_HOME/bin/native-image --pgo ListDir -o listdir-optimized
./listdir-optimized $1
time java ListDir $1
time ./listdir $1