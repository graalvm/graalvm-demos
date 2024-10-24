#!/bin/sh

set +e 

docker images -q hello | awk '{print($1)}' | xargs docker rmi || true
