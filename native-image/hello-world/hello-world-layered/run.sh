#!/usr/bin/env bash
set -ex

# Run from a class file
javac -d build src/com/example/HelloWorld.java
java -cp ./build com.example.HelloWorld

# Run from a JAR file
jar --create --file HelloWorld.jar --main-class com.example.HelloWorld -C build .
java -jar HelloWorld.jar

# Run from a native executable
mkdir -p native-build
native-image -H:LayerCreate=libjavabaselayer.nil,module=java.base -cp ./build -o libjavabaselayer -H:Path=./native-build
native-image -H:LayerUse=native-build/libjavabaselayer.nil -cp ./build -H:LinkerRPath="\$ORIGIN/native-build" -jar HelloWorld.jar
./HelloWorld
