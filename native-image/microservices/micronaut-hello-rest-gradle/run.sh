#!/usr/bin/env bash
set -ex

./gradlew nativeCompile
./build/native/nativeCompile/MnHelloRest &
sleep 5
curl "http://localhost:8080/hello"