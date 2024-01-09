#!/bin/sh

set -e

TOOLCHAIN_DIR=`pwd`/../x86_64-linux-musl-native
CC=${TOOLCHAIN_DIR}/bin/gcc
PATH=${TOOLCHAIN_DIR}/bin:${PATH}

# Compile Java source file
javac Hello.java

# Compile Java bytecodes into a fully statically linked executable
native-image -Ob --static --libc=musl -o hello Hello
rm -rf *.txt

# Create a compressed version of the executable
../upx --lzma --best hello -o hello.upx

# Package the compressed executable in a simple scratch container image
docker build . -t hello:upx