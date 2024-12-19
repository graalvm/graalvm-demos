#!/bin/sh

set +e

docker images jwebserver -q | grep -v TAG | awk '{print($1)}' | xargs docker rmi -f