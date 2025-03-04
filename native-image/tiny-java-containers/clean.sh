#!/bin/sh

set +e

cd helloworld
./clean.sh || true
cd ../jwebserver
./clean.sh || true