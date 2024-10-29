#!/bin/sh 

./build-dynamic.sh
./build-mostly.sh
./build-static.sh
./build-jlink.sh
./build-jvm.sh

echo "Generated Docker Container Images"
docker images jwebserver
