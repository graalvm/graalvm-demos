#!/usr/bin/env bash
set -ex

# Run from a class file
javac -d build src/com/example/HelloWorld.java
java -cp ./build com.example.HelloWorld

# Run from a JAR file
jar --create --file HelloWorld.jar --main-class com.example.HelloWorld -C build .
java -jar HelloWorld.jar

# Run from a native executable
native-image -jar HelloWorld.jar
./HelloWorld