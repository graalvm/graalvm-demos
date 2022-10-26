#!/bin/sh

TOOLCHAIN_DIR=`pwd`/../x86_64-linux-musl-native
CC=${TOOLCHAIN_DIR}/bin/gcc
PATH=${TOOLCHAIN_DIR}/bin:${PATH}

native-image --static --libc=musl -m jdk.httpserver -o jwebserver.static

# Scratch-nothing
docker build . -f Dockerfile.scratch.static -t jwebserver:scratch.static

# Distroless Static-no glibc
docker build . -f Dockerfile.distroless-static.static -t jwebserver:distroless-static.static

# Alpine-no glibc
docker build . -f Dockerfile.alpine.static -t jwebserver:alpine.static

# Compress with UPX
rm -f jwebserver.static-upx
upx --lzma --best -o jwebserver.static-upx jwebserver.static 

# Scratch--fully static and compressed
docker build . -f Dockerfile.scratch.static-upx -t jwebserver:scratch.static-upx