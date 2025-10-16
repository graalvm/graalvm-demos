#!/usr/bin/env bash
set -ex

./mvnw --no-transfer-progress clean install -Dpackaging=native-image -Pbase-layer
./mvnw --no-transfer-progress clean package -Dpackaging=native-image -Papp-layer
./app-layer-target/layered-app &
sleep 5
curl "http://localhost:8080/hello"
