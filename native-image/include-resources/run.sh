#!/usr/bin/env bash
set -ex

javac Fortune.java
native-image -Ob Fortune
./fortune