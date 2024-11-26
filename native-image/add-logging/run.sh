#!/usr/bin/env bash
set -ex

# Initialize a Logger at Build Time
$JAVA_HOME/bin/javac LoggerBuildTimeInit.java
$JAVA_HOME/bin/native-image -Ob --initialize-at-build-time=LoggerBuildTimeInit LoggerBuildTimeInit
./loggerbuildtimeinit

#  Initialize a Logger at Run time
$JAVA_HOME/bin/javac LoggerRunTimeInit.java
$JAVA_HOME/bin/native-image -Ob -H:IncludeResources="logging.properties" LoggerRunTimeInit
./loggerruntimeinit