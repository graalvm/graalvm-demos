#!/usr/bin/env bash

set -ex

$GRAALVM_HOME/bin/node --jvm --polyglot server.js
