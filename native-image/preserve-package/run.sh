#!/usr/bin/env bash
set -euo pipefail
set +x

run_reflection_example() {
  local expected="$1"
  shift

  local output
  output=$("$@")
  if [ "$output" != "$expected" ]; then
    echo "Error: expected '$expected' but got '$output'"
    exit 1
  fi
}

#
# Java build
#
./mvnw clean package
run_reflection_example \
  olleh \
  "$JAVA_HOME/bin/java" -jar target/preserve-package-1.0-SNAPSHOT.jar \
  org.graalvm.example.action.StringReverser reverse hello
run_reflection_example \
  HELLO \
  "$JAVA_HOME/bin/java" -jar target/preserve-package-1.0-SNAPSHOT.jar \
  org.graalvm.example.action.StringCapitalizer capitalize hello

#
# Default Native Image Build (no preserve)
#
set +e
./mvnw package -Pnative-default
./target/example-default \
  org.graalvm.example.action.StringReverser reverse hello
if [ $? -eq 0 ]; then
  echo "Error: native executable should exit with error"
  exit 1
fi
set -e

if [ ! -f target/example-default-build-report.html ]; then
  echo "Error: build report not found!"
  exit 1
fi

#
# Native Image build with Preserve
#
./mvnw package -Pnative-preserve
run_reflection_example \
  olleh \
  ./target/example-preserve \
  org.graalvm.example.action.StringReverser reverse hello
run_reflection_example \
  HELLO \
  ./target/example-preserve \
  org.graalvm.example.action.StringCapitalizer capitalize hello
