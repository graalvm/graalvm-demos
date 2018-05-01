#!/bin/bash

set -e

# Resolve the location of this script
source="${BASH_SOURCE[0]}"
while [ -h "$source" ] ; do
  prev_source="$source"
  source="$(readlink "$source")";
  if [[ "$source" != /* ]]; then
    # if the link was relative, it was relative to where it came from
    dir="$( cd -P "$( dirname "$prev_source" )" && pwd )"
    source="$dir/$source"
  fi
done
dir="$( cd -P "$( dirname "$source" )" && pwd )"

: ${GRAALVM_DIR?"GRAALVM_DIR must point to a GraalVM image"}

rm -f ${dir}/test.png
echo Testing GraalVM fastr_javaui example
if ! ${GRAALVM_DIR}/bin/java -cp ${dir}/bin -Dfastrjavaui.dest=${dir}/test.png com.oracle.truffle.r.fastrjavaui.FastRJavaCmd | grep "SUCCESS" > /dev/null; then
  echo "expected output not found"
  exit 1
fi
if [ ! -f ${dir}/test.png ]; then
  echo "expected file not generated"
  exit 1
fi
echo DONE
