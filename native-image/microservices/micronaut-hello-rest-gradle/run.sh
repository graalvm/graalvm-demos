#!/usr/bin/env bash
set -ex

./gradlew nativeCompile
./build/native/nativeCompile/hello &
sleep 5
curl "http://localhost:8080/hello"