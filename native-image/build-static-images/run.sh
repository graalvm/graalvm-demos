#!/usr/bin/env bash
set -ex

javac EnvMap.java
# Run fully static image build
native-image --static --libc=musl EnvMap -o static-envmap
# Run mostly-static image build
native-image -Ob --static-nolibc EnvMap -o mostly-static-envmap