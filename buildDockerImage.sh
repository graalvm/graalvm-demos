#!/bin/bash

set -e
set -u
set -o pipefail

BASE_GRAALVM_VERSION="21.2.0"
GRAALVM_JDK_VERSION="java11"
GRAALVM_TYPE="all"   # other types are "native" or "polyglot"

DEFAULT_GRAALVM_VERSION="${BASE_GRAALVM_VERSION}-${GRAALVM_JDK_VERSION}-${GRAALVM_TYPE}"

FULL_GRAALVM_VERSION="${FULL_GRAALVM_VERSION:-"${DEFAULT_GRAALVM_VERSION}"}"
IMAGE_VERSION="0.1"
FULL_DOCKER_TAG_NAME="graalvm/demos"
GRAALVM_HOME_FOLDER="/graalvm"
WORKDIR="graalvm-demos"

echo "--- Building dockr image for ${WORKDIR}"; echo
time docker build                                                       \
	             --build-arg GRAALVM_HOME="${GRAALVM_HOME_FOLDER}"      \
	             --build-arg IMAGE_VERSION=${IMAGE_VERSION}             \
                 --build-arg FULL_GRAALVM_VERSION=${FULL_GRAALVM_VERSION} \
	             -t ${FULL_DOCKER_TAG_NAME}:${IMAGE_VERSION}            \
	             "."

IMAGE_IDS="$(docker images -f dangling=true -q || true)"
if [[ -n ${IMAGE_IDS} ]]; then
    echo "--- Cleaning up image(s)"; echo
    docker rmi -f ${IMAGE_IDS} || true
fi