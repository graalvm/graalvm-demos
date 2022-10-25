#!/bin/sh

set -x 

./build-dynamic.sh
./build-mostly.sh
./build-static.sh
./build-jlink.sh
rm *.txt

echo "Generated Executables"
ls -lh jwebserver*

echo "Generated Docker Container Images"
docker images jwebserver
