#!/bin/sh

set -x

rm -rf  x86_64-linux-musl-native  zlib-*
./helloworld/clean.sh
./jwebserver/clean.sh