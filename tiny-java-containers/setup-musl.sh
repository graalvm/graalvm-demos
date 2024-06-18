#!/bin/sh
set -e

ZLIB_VERSION=1.2.13
TOOLCHAIN_DIR=`pwd`/musl-1.2.4

# Download musl
wget -q https://musl.libc.org/releases/musl-1.2.4.tar.gz
tar -xzf musl-1.2.4.tar.gz
rm musl-1.2.4.tar.gz
cd musl-1.2.4
./configure
make
make install 
cd ..

# Download, build, install zlib into TOOLCHAIN_DIR
echo "zlib version=${ZLIB_VERSION}"
wget -q https://zlib.net/fossils/zlib-${ZLIB_VERSION}.tar.gz
tar -xzf zlib-${ZLIB_VERSION}.tar.gz
rm zlib-${ZLIB_VERSION}.tar.gz
cd zlib-${ZLIB_VERSION}
./configure --prefix=${TOOLCHAIN_DIR} --static
make
make install
cd ..