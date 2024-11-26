#!/usr/bin/env bash
set -ex

$JAVA_HOME/bin/javac -d build src/com/example/App.java
$JAVA_HOME/bin/jar --create --file App.jar --main-class com.example.App -C build .
$JAVA_HOME/bin/native-image -Ob -jar App.jar
./App