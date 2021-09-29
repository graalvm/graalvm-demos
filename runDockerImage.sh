#!/bin/bash

set -e
set -u
set -o pipefail

BASE_GRAALVM_VERSION="21.2.0"
GRAALVM_JDK_VERSION="java11"
GRAALVM_TYPE="all"   # other types are "native" or "polyglot"

DEFAULT_GRAALVM_VERSION="${BASE_GRAALVM_VERSION}-${GRAALVM_JDK_VERSION}-${GRAALVM_TYPE}"

FULL_GRAALVM_VERSION="${FULL_GRAALVM_VERSION:-"${DEFAULT_GRAALVM_VERSION}"}"
FULL_DOCKER_TAG_NAME="graalvm/demos"
GRAALVM_HOME_FOLDER="/graalvm"
WORKDIR="/graalvm-demos"

M2_INSIDE_CONTAINER="/root/.m2/"
M2_ON_HOST="_dot_m2_folder"

## By doing this we are extracting away the .m2 folder from inside the container to the outside,
## giving us a few advantages: speed gains, reduced image size, among others
echo; echo "-- Creating/using folder '${M2_ON_HOST}' mounted as '.m2' folder inside the container (${M2_INSIDE_CONTAINER})"
mkdir -p ${M2_ON_HOST}

echo; echo "--- Running docker image for GraalVM version ${FULL_GRAALVM_VERSION} for ${WORKDIR}"; echo
docker run --rm                                                    \
            --interactive --tty                                    \
	        --volume $(pwd):/${WORKDIR}                            \
        	-p 8888:8888                                           \
        	-p 8088:8088                                           \
        	-p 8080:8080                                           \
        	--workdir ${WORKDIR}                                   \
        	--volume "${PWD}/${M2_ON_HOST}":${M2_INSIDE_CONTAINER} \
        	--env GRAALVM_HOME=${GRAALVM_HOME_FOLDER}              \
        	--entrypoint /bin/bash                                 \
        	${FULL_DOCKER_TAG_NAME}:${FULL_GRAALVM_VERSION}