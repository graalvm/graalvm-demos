#!/usr/bin/env bash
set -ex

mvn --no-transfer-progress package
$JAVA_HOME/bin/java --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule
$JAVA_HOME/bin/native-image -Ob --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule
./hellomodule