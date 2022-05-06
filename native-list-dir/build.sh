#!/usr/bin/env bash
set -ex

$GRAALVM_HOME/bin/javac ListDir.java
$GRAALVM_HOME/bin/native-image ListDir

$GRAALVM_HOME/bin/javac ExtListDir.java
$GRAALVM_HOME/bin/native-image --language:js ExtListDir
