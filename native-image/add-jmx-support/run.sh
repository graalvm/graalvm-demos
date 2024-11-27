#!/usr/bin/env bash
set -ex

$JAVA_HOME/bin/javac SimpleJmx.java
$JAVA_HOME/bin/native-image -Ob --enable-monitoring=jmxserver,jmxclient,jvmstat -H:DynamicProxyConfigurationFiles=proxy-config.json SimpleJmx
./simplejmx -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=9996 -Dcom.sun.management.jmxremote.ssl=false