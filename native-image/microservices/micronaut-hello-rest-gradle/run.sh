#!/usr/bin/env bash
set -euo pipefail
set -x

wait_for_hello() {
  local port="$1"
  local attempts=30

  while (( attempts > 0 )); do
    if response=$(curl --silent --show-error --fail "http://localhost:${port}/GraalVM"); then
      [ "$response" = "Hello GraalVM" ]
      return 0
    fi
    attempts=$((attempts - 1))
    sleep 1
  done

  echo "Timed out waiting for Micronaut app on port ${port}" >&2
  return 1
}

./gradlew run &
jvm_pid=$!
trap 'kill "$jvm_pid" 2>/dev/null || true' EXIT
wait_for_hello 8080
kill "$jvm_pid"
wait "$jvm_pid" || true
trap - EXIT

./gradlew nativeCompile
./build/native/nativeCompile/hello &
native_pid=$!
trap 'kill "$native_pid" 2>/dev/null || true' EXIT
wait_for_hello 8080
kill "$native_pid"
wait "$native_pid" || true
trap - EXIT
