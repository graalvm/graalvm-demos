#!/usr/bin/env bash
set -euo pipefail
set -x

extract_sbom() {
  local image_path="$1"

  if command -v native-image-utils >/dev/null 2>&1; then
    native-image-utils extract-sbom --image-path="$image_path"
  elif [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/native-image-utils" ]; then
    "$JAVA_HOME/bin/native-image-utils" extract-sbom --image-path="$image_path"
  elif command -v native-image-inspect >/dev/null 2>&1; then
    native-image-inspect --sbom "$image_path"
  elif [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/native-image-inspect" ]; then
    "$JAVA_HOME/bin/native-image-inspect" --sbom "$image_path"
  else
    echo "No SBOM extraction tool found (expected native-image-utils or native-image-inspect)." >&2
    exit 1
  fi
}

native-image -Ob -m jdk.httpserver -o jwebserver
jwebserver_sbom=$(extract_sbom ./jwebserver)
printf '%s\n' "$jwebserver_sbom"
printf '%s\n' "$jwebserver_sbom" | grep -q '"bomFormat"[[:space:]]*:[[:space:]]*"CycloneDX"'

# Requires GraalVM 25 or later:
cd sbom-test
mvn clean package
mvn -Pnative package

sbom_test_image=target/sbom-test
[ -x "$sbom_test_image" ]
sbom_test_sbom=$(extract_sbom "$sbom_test_image")
printf '%s\n' "$sbom_test_sbom"
printf '%s\n' "$sbom_test_sbom" | grep -q 'commons-validator'
printf '%s\n' "$sbom_test_sbom" | grep -q '"bomFormat"[[:space:]]*:[[:space:]]*"CycloneDX"'
