#!/usr/bin/env bash
set -ex

./mvnw --no-transfer-progress clean package
./mvnw --no-transfer-progress package -Dpackaging=native-image
./target/MnHelloRest &
sleep 5
curl "http://localhost:8080/hello"