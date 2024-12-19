#!/bin/sh

# Scratch-nothing
docker build . -f Dockerfile.scratch.static -t jwebserver:scratch.static

# Distroless Static-no glibc
docker build . -f Dockerfile.distroless-static.static -t jwebserver:distroless-static.static

# Alpine-no glibc
docker build . -f Dockerfile.alpine.static -t jwebserver:alpine.static

# Scratch--fully static and compressed
docker build . -f Dockerfile.scratch.static-upx -t jwebserver:scratch.static-upx
