#!/usr/bin/env bash
set -xe

time java -cp ./target/mixed-code-hello-world-1.0-SNAPSHOT-jar-with-dependencies.jar hello.JavaHello

time ./helloworld
