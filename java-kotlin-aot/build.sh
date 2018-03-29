#!/usr/bin/env bash
set -ex

mvn clean install

$GRAALVM_HOME/bin/native-image -cp ./target/mixed-code-hello-world-1.0-SNAPSHOT.jar -H:Name=helloworld -H:Class=hello.JavaHello -H:+ReportUnsupportedElementsAtRuntime
