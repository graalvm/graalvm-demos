#!/usr/bin/env bash
set -ex

javac Streams.java
java Streams 100000 200
$JAVA_HOME/bin/native-image --pgo-instrument Streams
./streams 100000 200
$JAVA_HOME/bin/native-image --pgo Streams
./streams  100000 200