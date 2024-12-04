#!/usr/bin/env bash
set -ex

# Dump the Initial Heap of a Native Executable
javac HelloWorld.java 
native-image -Ob HelloWorld --enable-monitoring=heapdump
./helloworld -XX:+DumpHeapAndExit

# Create a Heap Dump with SIGUSR1 (Linux/macOS only)
javac SVMHeapDump.java
native-image -Ob SVMHeapDump --enable-monitoring=heapdump
./svmheapdump

# Create a Heap Dump from within a Native Executable
javac SVMHeapDumpAPI.java
native-image -Ob SVMHeapDumpAPI
./svmheapdumpapi --heapdump