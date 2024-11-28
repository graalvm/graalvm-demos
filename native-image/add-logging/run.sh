#!/usr/bin/env bash
set -ex

# Initialize a Logger at Build Time
javac LoggerBuildTimeInit.java
native-image -Ob --initialize-at-build-time=LoggerBuildTimeInit LoggerBuildTimeInit
./loggerbuildtimeinit

#  Initialize a Logger at Run time
javac LoggerRunTimeInit.java
native-image -Ob -H:IncludeResources="logging.properties" LoggerRunTimeInit
./loggerruntimeinit