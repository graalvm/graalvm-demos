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

${dir}/weather_predictor/test.sh
${dir}/fastr_javaui/test.sh
${dir}/fastr_node/test.sh
${dir}/fastr_scalar/run.sh
# rJava needs to be installed. See ${dir}/fastr_rJava/README.md
${dir}/fastr_rJava/run.sh
