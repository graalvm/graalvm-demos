#!/bin/sh

rm hello hello.upx
rm *.txt
rm *.class
docker images -q hello | awk '{print($1)}' | xargs docker rmi
