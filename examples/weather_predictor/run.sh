#!/bin/bash

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

# Completely silent execution not reporting any exceptions/errors from Graal
# It is possible to specify --inspect and --inspect.Suspend=false and/or --agent as arguments
${GRAALVM_DIR}/bin/node ${GRAALVM_ADDITIONAL_ARGS} --jvm --jvm.Xss2m --jvm.Dtruffle.js.NashornJavaInterop=true --jvm.Dgraal.CompilationFailureAction=Silent --jvm.Djava.util.logging.config.file=${dir}/java_options --jvm.classpath=${dir}/bin --ruby.load_paths=${dir}/openweather/lib,${dir} --polyglot "$@" ${dir}/weatherServer.js
