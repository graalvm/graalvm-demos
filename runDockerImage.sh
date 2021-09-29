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
WORKDIR="graalvm-demos"

echo; echo "--- Running GraalVM docker image ${FULL_GRAALVM_VERSION} for ${WORKDIR}"; echo
docker run --rm                                       \
            --interactive --tty                       \
	        --volume $(pwd):/${WORKDIR}               \
        	-p 8888:8888                              \
        	--workdir /${WORKDIR}                     \
        	--env GRAALVM_HOME=${GRAALVM_HOME_FOLDER} \
        	--entrypoint /bin/bash      \
        	${FULL_DOCKER_TAG_NAME}:${FULL_GRAALVM_VERSION}