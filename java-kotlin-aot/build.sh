#!/usr/bin/env bash
set -ex

mvn clean install

$JAVA_HOME/bin/native-image --no-fallback -cp ./target/mixed-code-hello-world-1.0-SNAPSHOT-jar-with-dependencies.jar -H:Name=helloworld -H:Class=hello.JavaHello -H:+ReportUnsupportedElementsAtRuntime