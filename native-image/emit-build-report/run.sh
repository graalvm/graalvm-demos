#!/usr/bin/env bash
set -ex

javac IthWord.java
native-image IthWord --emit build-report
./ithword 1

cp optimized/IthWord.java IthWord.java
javac IthWord.java
native-image IthWord --emit build-report
./ithword 1