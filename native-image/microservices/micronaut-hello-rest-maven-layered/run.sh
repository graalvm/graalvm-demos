#!/usr/bin/env bash
set -ex

./mvnw --no-transfer-progress clean install -Dpackaging=native-image -Pbase-layer
./mvnw --no-transfer-progress clean package -Dpackaging=native-image -Papp-layer
./app-layer-target/layered-app &
pid=$!
sleep 5
curl "http://localhost:8080/hello"
kill $pid

cd base-layer
../mvnw --no-transfer-progress clean install -Dpackaging=native-image
cd ..
./mvnw --no-transfer-progress clean package -Dpackaging=native-image -Pmicronaut-app-layer
./app-layer-target/layered-app &
sleep 5
curl "http://localhost:8080/hello"
