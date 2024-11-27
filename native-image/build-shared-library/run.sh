#!/usr/bin/env bash
set -ex

javac LibEnvMap.java
native-image -o libenvmap --shared 
clang -I ./ -L ./ -l envmap -Wl,-rpath ./ -o main main.c
./main USER
