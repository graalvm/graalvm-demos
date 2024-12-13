#!/usr/bin/env bash
set -ex

javac ReadProperties.java
native-image -Ob -Dstatic_key=STATIC_VALUE ReadProperties
./readproperties -Dinstance_key=INSTANCE_VALUE
native-image -Ob --initialize-at-build-time=ReadProperties -Dstatic_key=STATIC_VALUE ReadProperties
./readproperties -Dinstance_key=INSTANCE_VALUE