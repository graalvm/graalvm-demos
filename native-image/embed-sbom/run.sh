#!/usr/bin/env bash
set -euo pipefail
set -x

extract_sbom() {
  local image_path="$1"
  local tool

  for tool in \
    native-image-utils \
    "${JAVA_HOME:-}/bin/native-image-utils" \
    native-image-inspect \
    "${JAVA_HOME:-}/bin/native-image-inspect"
  do
    [ -x "$tool" ] || command -v "$tool" >/dev/null 2>&1 || continue

    case "$(basename "$tool")" in
      native-image-utils)
        "$tool" extract-sbom --image-path="$image_path"
        ;;
      native-image-inspect)
        "$tool" --sbom "$image_path"
        ;;
    esac
    return 0
  done

  echo "No SBOM extraction tool found (expected native-image-utils or native-image-inspect)." >&2
  exit 1
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
