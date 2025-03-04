#!/bin/sh

set +e

# Package the compressed executable in a simple scratch container image
docker build . -t hello:upx
docker images hello