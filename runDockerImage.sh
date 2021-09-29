#!/bin/bash

set -e
set -u
set -o pipefail

BASE_GRAALVM_VERSION="21.2.0"
GRAALVM_JDK_VERSION="java11"
GRAALVM_TYPE="all"   # other types are "native" or "polyglot"

DEFAULT_GRAALVM_VERSION="${BASE_GRAALVM_VERSION}-${GRAALVM_JDK_VERSION}-${GRAALVM_TYPE}"

GRAALVM_VERSION="${GRAALVM_VERSION:-"${DEFAULT_GRAALVM_VERSION}"}"
IMAGE_VERSION="0.1"
FULL_DOCKER_TAG_NAME="graalvm/demos"
GRAALVM_HOME_FOLDER="/graalvm"
WORKDIR="graalvm-demos"

docker run --rm                                       \
            --interactive --tty                       \
	        --volume $(pwd):/${WORKDIR}               \
        	-p 8888:8888                              \
        	--workdir /${WORKDIR}                     \
        	--env GRAALVM_HOME=${GRAALVM_HOME_FOLDER} \
        	--entrypoint /bin/bash      \
        	${FULL_DOCKER_TAG_NAME}:${IMAGE_VERSION}