#!/bin/bash

set -e
set -u
set -o pipefail

BASE_GRAALVM_VERSION="21.2.0"
GRAALVM_JDK_VERSION="java11"
GRAALVM_TYPE="all"   # other types are "native" or "polyglot"
# See https://hub.docker.com/r/findepi/graalvm/tags, for details on version tag names
DEFAULT_GRAALVM_VERSION="${BASE_GRAALVM_VERSION}-${GRAALVM_JDK_VERSION}-${GRAALVM_TYPE}"

FULL_GRAALVM_VERSION="${1:-"${DEFAULT_GRAALVM_VERSION}"}"
FULL_DOCKER_TAG_NAME="graalvm/demos"
GRAALVM_HOME_FOLDER="/graalvm"

MAVEN_VERSION="3.8.3"
GRADLE_VERSION="7.2"
SCALA_VERSION="3.0.2"
SBT_VERSION="1.5.5"
WORKDIR="/graalvm-demos"


# Check if the 'findepi/graalvm' image version tag exists, else print additional steps information
IMAGE_EXISTS=$(docker inspect --type=image "findepi/graalvm:${FULL_GRAALVM_VERSION}" || true)
if [[ ${IMAGE_EXISTS} == '[]' ]]; then
    echo ""
    echo "A GraalVM Docker image with \"${FULL_GRAALVM_VERSION}\" as the version tag does not exist in the local or remote Docker registry."
    echo ""
    echo "A valid version tag name would like this: '21.2.0-java11-all'. Please check https://hub.docker.com/r/findepi/graalvm/tags, to verify if this tag is valid and exists, or pick a valid one from there."
    echo ""
    exit
fi

# Building wrk takes a while
echo; echo "--- Building docker image for 'wrk' utility: workload generator"; echo
time docker build                          \
	             -t workload-generator/wrk \
	             -f Dockerfile-wrk "."


# Building micronaut-starter docker image is relatively quicker
echo; echo "--- Building Docker image for micronaut-starter:${FULL_GRAALVM_VERSION}"; echo
time docker build                                                         \
	             --build-arg GRAALVM_HOME="${GRAALVM_HOME_FOLDER}"        \
                 --build-arg FULL_GRAALVM_VERSION=${FULL_GRAALVM_VERSION} \
	             -t micronaut/micronaut-starter:${FULL_GRAALVM_VERSION}   \
	             -f Dockerfile-mn "."


# Building graalvm-demos docker image is relatively quicker
echo; echo "--- Building Docker image for GraalVM version ${FULL_GRAALVM_VERSION} for ${WORKDIR}"; echo
time docker build                                                         \
	             --build-arg GRAALVM_HOME="${GRAALVM_HOME_FOLDER}"        \
                 --build-arg FULL_GRAALVM_VERSION=${FULL_GRAALVM_VERSION} \
                 --build-arg MAVEN_VERSION=${MAVEN_VERSION}               \
                 --build-arg GRADLE_VERSION=${GRADLE_VERSION}             \
                 --build-arg SCALA_VERSION=${SCALA_VERSION}               \
                 --build-arg SBT_VERSION=${SBT_VERSION}                   \
                 --build-arg WORKDIR=${WORKDIR}                           \
	             -t ${FULL_DOCKER_TAG_NAME}:${FULL_GRAALVM_VERSION}       \
	             "."

IMAGE_IDS="$(docker images -f dangling=true -q || true)"
if [[ -n ${IMAGE_IDS} ]]; then
    echo; echo "--- Cleaning up image(s)"; echo
    docker rmi -f ${IMAGE_IDS} || true
fi