#!/usr/bin/env bash
set +e

./mvnw package
$JAVA_HOME/bin/java -jar target/preserve-package-1.0-SNAPSHOT.jar \
       org.graalvm.example.action.StringReverser reverse "hello"

./mvnw package -Pnative-default
./target/example-default \
       org.graalvm.example.action.StringReverser reverse "hello"

./mvnw package -Pnative-preserve
./target/example-preserve \
       org.graalvm.example.action.StringReverser reverse "hello"