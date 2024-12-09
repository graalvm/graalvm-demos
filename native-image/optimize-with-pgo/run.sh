#!/usr/bin/env bash
set -ex

javac Streams.java
java Streams 100000 200
native-image -Ob --pgo-instrument Streams
./streams 100000 200
native-image -Ob --pgo Streams
./streams  100000 200