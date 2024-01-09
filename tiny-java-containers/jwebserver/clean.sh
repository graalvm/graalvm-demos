#!/bin/sh

set +e

rm -rf jwebserver-jlink/
rm jwebserver.dynamic  jwebserver.mostly  jwebserver.static  jwebserver.static-upx
rm -rf svm*.md
docker images jwebserver -q | grep -v TAG | awk '{print($1)}' | xargs docker rmi