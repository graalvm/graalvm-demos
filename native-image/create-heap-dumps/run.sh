#!/usr/bin/env bash
set -euo pipefail
set -x

rm -f ./*.hprof

# Dump the Initial Heap of a Native Executable
javac HelloWorld.java 
native-image -Ob HelloWorld --enable-monitoring=heapdump
./helloworld -XX:+DumpHeapAndExit
find . -maxdepth 1 -name '*.hprof' | grep -q .

rm -f ./*.hprof

# Create a Heap Dump with SIGUSR1 (Linux/macOS only)
javac SVMHeapDump.java
native-image -Ob SVMHeapDump --enable-monitoring=heapdump
./svmheapdump &
pid=$!
sleep 2
kill -SIGUSR1 "$pid"
wait "$pid"
find . -maxdepth 1 -name '*.hprof' | grep -q .

rm -f ./*.hprof

# Create a Heap Dump from within a Native Executable
javac SVMHeapDumpAPI.java
native-image -Ob SVMHeapDumpAPI
api_output=$(./svmheapdumpapi --heapdump)
printf '%s\n' "$api_output"
printf '%s\n' "$api_output" | grep -q 'Heap dump created '
