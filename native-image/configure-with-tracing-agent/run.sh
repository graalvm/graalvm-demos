#!/usr/bin/env bash
set -ex

javac ReflectionExample.java
java ReflectionExample StringReverser reverse "hello"
java ReflectionExample StringCapitalizer capitalize "hello"
mkdir -p META-INF/native-image
java -agentlib:native-image-agent=config-output-dir=META-INF/native-image ReflectionExample StringReverser reverse "hello"
java -agentlib:native-image-agent=config-merge-dir=META-INF/native-image ReflectionExample StringCapitalizer capitalize "hello"
native-image -Ob ReflectionExample
./reflectionexample StringReverser reverse "hello"
./reflectionexample StringCapitalizer capitalize "hello"