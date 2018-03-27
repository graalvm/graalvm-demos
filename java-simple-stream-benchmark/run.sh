#!/usr/bin/env bash
set -xe

$GRAALVM_HOME/bin/java -jar target/benchmarks.jar
