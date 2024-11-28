#!/usr/bin/env bash
set -ex

javac JFRDemo.java
native-image -Ob --enable-monitoring=jfr JFRDemo
./jfrdemo -XX:StartFlightRecording=filename=recording.jfr
jfr print recording.jfr