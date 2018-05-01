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

cran_mirror=http://cran.us.r-project.org
if [ ! -d ${dir}/openweather ]; then
  echo "Cloning 'openweather'"
  git -C ${dir} clone https://github.com/lucasocon/openweather.git
  git -C ${dir}/openweather checkout d5f49d3c567bd1ac3e055a65189661d8d3851c7f
fi

# Install expressjs
echo "Running 'npm install'"
${GRAALVM_DIR}/bin/npm --prefix ${dir} install ${dir}

echo "Compiling the Java sources"
mkdir -p ${dir}/bin
${GRAALVM_DIR}/bin/javac -d ${dir}/bin ${dir}/java/com/oracle/graalvm/demo/weather/*.java
