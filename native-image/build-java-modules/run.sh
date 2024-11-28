#!/usr/bin/env bash
set -ex

mvn --no-transfer-progress package
java --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule
native-image -Ob --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule
./hellomodule