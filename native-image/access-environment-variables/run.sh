#!/usr/bin/env bash
set -ex

javac EnvMap.java
native-image -Ob EnvMap

./envmap HELLO
export HELLOWORLD='Hello World!'
./envmap HELLO