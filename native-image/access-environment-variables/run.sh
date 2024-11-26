#!/usr/bin/env bash
set -ex

$JAVA_HOME/bin/javac EnvMap.java
$JAVA_HOME/bin/native-image -Ob EnvMap

./envmap HELLO
export HELLOWORLD='Hello World!'
./envmap HELLO