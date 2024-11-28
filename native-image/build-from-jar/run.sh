#!/usr/bin/env bash
set -ex

javac -d build src/com/example/App.java
jar --create --file App.jar --main-class com.example.App -C build .
native-image -Ob -jar App.jar
./App