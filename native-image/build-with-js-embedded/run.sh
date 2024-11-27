#!/usr/bin/env bash
set -ex

mvn --no-transfer-progress package
mvn -Pnative package
./target/PrettyPrintJSON <<EOF
{"GraalVM":{"description":"Language Abstraction Platform","supports":["combining languages","embedding languages","creating native images"],"languages": ["Java", "JavaScript", "Python"]}}
EOF