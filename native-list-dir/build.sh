#!/usr/bin/env bash
set -ex

$JAVA_HOME/bin/javac ListDir.java
$JAVA_HOME/bin/native-image ListDir

#javac ExtListDir.java
#$GRAALVM_HOME/bin/native-image --language:js ExtListDir
