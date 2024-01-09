#!/bin/sh

set +e

rm -rf  x86_64-linux-musl-native  zlib-*
cd helloworld
./clean.sh || true
cd ../jwebserver
./clean.sh || true