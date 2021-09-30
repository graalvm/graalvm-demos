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

MAVEN_VERSION="3.8.2"
WORKDIR="/graalvm-demos"

# Building wrk takes a while
echo; echo "--- Building docker image for 'wrk' utility: workload generator"; echo
time docker build                          \
	             -t workload-generator/wrk \
	             -f Dockerfile-wrk "."


# Building micronaut-starter docker image is relatively quicker
echo; echo "--- Building docker image for GraalVM version ${FULL_GRAALVM_VERSION} for ${WORKDIR}"; echo
time docker build                                                         \
	             --build-arg GRAALVM_HOME="${GRAALVM_HOME_FOLDER}"        \
                 --build-arg FULL_GRAALVM_VERSION=${FULL_GRAALVM_VERSION} \
	             -t micronaut/micronaut-starter                           \
	             -f Dockerfile-mn "."


# Building graalvm-demos docker image is relatively quicker
echo; echo "--- Building docker image for GraalVM version ${FULL_GRAALVM_VERSION} for ${WORKDIR}"; echo
time docker build                                                         \
	             --build-arg GRAALVM_HOME="${GRAALVM_HOME_FOLDER}"        \
                 --build-arg FULL_GRAALVM_VERSION=${FULL_GRAALVM_VERSION} \
                 --build-arg MAVEN_VERSION=${MAVEN_VERSION}               \
                 --build-arg WORKDIR=${WORKDIR}                           \
	             -t ${FULL_DOCKER_TAG_NAME}:${FULL_GRAALVM_VERSION}       \
	             "."

IMAGE_IDS="$(docker images -f dangling=true -q || true)"
if [[ -n ${IMAGE_IDS} ]]; then
    echo; echo "--- Cleaning up image(s)"; echo
    docker rmi -f ${IMAGE_IDS} || true
fi