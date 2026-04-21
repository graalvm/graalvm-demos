#!/usr/bin/env bash
set -ex

javac ListDir.java
java ListDir ..
native-image ListDir
./listdir ..

native-image -Ob --pgo-instrument ListDir -o listdir-instrumented
./listdir-instrumented ..
native-image -Ob --pgo ListDir -o listdir-optimized
./listdir-optimized ..
