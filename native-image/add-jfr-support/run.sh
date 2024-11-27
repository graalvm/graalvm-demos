#!/usr/bin/env bash
set -ex

$JAVA_HOME/bin/javac JFRDemo.java
$JAVA_HOME/bin/native-image -Ob --enable-monitoring=jfr JFRDemo
./jfrdemo -XX:StartFlightRecording=filename=recording.jfr
jfr print recording.jfr