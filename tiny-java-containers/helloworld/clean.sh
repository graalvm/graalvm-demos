#!/bin/sh

set +e 

rm -rf hello hello.upx
rm -rf *.txt
rm -rf *.class
docker images -q hello | awk '{print($1)}' | xargs docker rmi || true
