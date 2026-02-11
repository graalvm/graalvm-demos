#!/usr/bin/env bash
set -ex

cd base-layer
../mvnw --no-transfer-progress clean install
cd ../spring-application-layer
../mvnw --no-transfer-progress clean package -Dpackaging=native-image -Pnative -Papp-layer
./target/spring-demo-layered &
sleep 5
curl "http://localhost:8080/hello"
