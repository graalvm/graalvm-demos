#!/usr/bin/env bash
set -ex

./mvnw -Pnative package
./target/fortune
./mvnw -Pnative-size-optimized package
./target/fortune-optimized
du -h target/fortune*