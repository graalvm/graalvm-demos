#!/usr/bin/env bash
set -ex

cd 1-original
javac IthWord.java
native-image IthWord --emit build-report
./ithword 1

cd ../2-optimized
javac IthWord.java
native-image IthWord --emit build-report
./ithword 1

cd ../3-optimized
javac IthWord.java
native-image IthWord --emit build-report
./ithword 1
