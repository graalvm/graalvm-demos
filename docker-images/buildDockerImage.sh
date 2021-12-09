#!/bin/bash

set -e
set -u
set -o pipefail

if [[ -n "${DOCKER_USER_NAME:-}" ]]; then
   echo "DOCKER_USER_NAME has been defined in the environment, hence docker image built WILL BE pushed to user's Docker hub."
   PUSH_IMAGE_TO_HUB="yes"
else 
   echo "DOCKER_USER_NAME with has NOT been defined in the environment, hence docker image built WILL NOT be pushed to user's Docker hub."
   PUSH_IMAGE_TO_HUB="no"
fi

DOCKER_USER_NAME="${DOCKER_USER_NAME:-neomatrix369}"
SOURCE_DOCKER_HUB="ghcr.io/graalvm/graalvm-ce"
BASE_GRAALVM_VERSION="21.2.0"
GRAALVM_JDK_VERSION="java11"
# See https://github.com/graalvm/container/pkgs/container/graalvm-ce for all tags related info
DEFAULT_GRAALVM_VERSION="${GRAALVM_JDK_VERSION}-${BASE_GRAALVM_VERSION}"
DOCKER_IMAGE_TAGS_WEBSITE="https://github.com/graalvm/container/pkgs/container/graalvm-ce"

FULL_GRAALVM_VERSION="${1:-"${DEFAULT_GRAALVM_VERSION}"}"
FULL_DOCKER_TAG_NAME="graalvm-demos"
GRAALVM_HOME_FOLDER="/opt/graalvm"

MAVEN_VERSION="3.8.4"
GRADLE_VERSION="7.2"
SCALA_VERSION="3.0.2"
SBT_VERSION="1.5.5"
WORKDIR="/graalvm-demos"
DEMO_TYPE="console"

findImage() {
    IMAGE_NAME=$1
    echo $(docker images ${IMAGE_NAME} -q | head -n1 || true)
}

pushImageToHub() {
    if [[ "${PUSH_IMAGE_TO_HUB}" == "no" ]]; then
        echo "Skipping the process of pushing built image to Dockerhub. Available in the local repository only."
        return        
    fi

    FULL_DOCKER_IMAGE_NAME="$1"

    IMAGE_FOUND="$(findImage ${FULL_DOCKER_IMAGE_NAME})"
    IS_FOUND="found"
    if [[ -z "${IMAGE_FOUND}" ]]; then
        IS_FOUND="not found"
    fi
    echo "Docker image '${FULL_DOCKER_IMAGE_NAME}' is ${IS_FOUND} in the local repository"

    docker tag ${IMAGE_FOUND} ${FULL_DOCKER_IMAGE_NAME}
    docker push ${FULL_DOCKER_IMAGE_NAME}
}

# Building wrk takes a while
echo; echo "--- Building docker image for 'wrk' utility: workload generator" >&2; echo
TARGET_IMAGE="${DOCKER_USER_NAME}/workload-generator"
docker pull ${TARGET_IMAGE} && echo "Finished pulling ${TARGET_IMAGE} from remote repo" || true
time docker build                      \
	             -t ${TARGET_IMAGE}    \
	             -f Dockerfile-wrk "."
pushImageToHub ${TARGET_IMAGE}


# Building micronaut-starter docker image is relatively quicker
TARGET_IMAGE="${DOCKER_USER_NAME}/micronaut-starter:${FULL_GRAALVM_VERSION}"
echo; echo "--- Building Docker image for ${TARGET_IMAGE}" >&2; echo
docker pull ${TARGET_IMAGE} && echo "Finished pulling ${TARGET_IMAGE} from remote repo" || true
time docker build                                                           \
	             --build-arg GRAALVM_HOME="${GRAALVM_HOME_FOLDER}"          \
                 --build-arg SOURCE_DOCKER_HUB="${SOURCE_DOCKER_HUB}"       \
                 --build-arg FULL_GRAALVM_VERSION="${FULL_GRAALVM_VERSION}" \
	             -t ${TARGET_IMAGE}                                         \
	             -f Dockerfile-mn "."
pushImageToHub ${TARGET_IMAGE}


# Building graalvm-demos (console) docker image is relatively quicker
TARGET_IMAGE="${DOCKER_USER_NAME}/${FULL_DOCKER_TAG_NAME}:${FULL_GRAALVM_VERSION}"
echo; echo "--- Building Docker image (console) for GraalVM version ${TARGET_IMAGE} for ${WORKDIR}" >&2; echo
docker pull ${TARGET_IMAGE} && echo "Finished pulling ${TARGET_IMAGE} from remote repo" || true
time docker build                                                         \
                 --build-arg DOCKER_USER_NAME=${DOCKER_USER_NAME}         \
	             --build-arg GRAALVM_HOME="${GRAALVM_HOME_FOLDER}"        \
                 --build-arg SOURCE_DOCKER_HUB="${SOURCE_DOCKER_HUB}"     \
                 --build-arg FULL_GRAALVM_VERSION="${FULL_GRAALVM_VERSION}" \
                 --build-arg MAVEN_VERSION=${MAVEN_VERSION}               \
                 --build-arg GRADLE_VERSION=${GRADLE_VERSION}             \
                 --build-arg SCALA_VERSION=${SCALA_VERSION}               \
                 --build-arg SBT_VERSION=${SBT_VERSION}                   \
                 --build-arg WORKDIR=${WORKDIR}                           \
	             -t ${TARGET_IMAGE}                                       \
	             "."
pushImageToHub ${TARGET_IMAGE}


# Building graalvm-demos (gui) docker image is relatively quicker
TARGET_IMAGE="${DOCKER_USER_NAME}/${FULL_DOCKER_TAG_NAME}-gui:${FULL_GRAALVM_VERSION}"
echo; echo "--- Building Docker image (gui) for GraalVM version ${TARGET_IMAGE} for ${WORKDIR}" >&2; echo
docker pull ${TARGET_IMAGE} && echo "Finished pulling ${TARGET_IMAGE} from remote repo" || true

time docker build                                                           \
                 --build-arg DOCKER_USER_NAME=${DOCKER_USER_NAME}           \
                 --build-arg SOURCE_DOCKER_HUB="${SOURCE_DOCKER_HUB}"       \
                 --build-arg FULL_GRAALVM_VERSION="${FULL_GRAALVM_VERSION}" \
                 -t ${TARGET_IMAGE}                                         \
                 -f Dockerfile-gui "."
pushImageToHub ${TARGET_IMAGE}


IMAGE_IDS="$(docker images -f dangling=true -q || true)"
if [[ -n ${IMAGE_IDS} ]]; then
    echo; echo "--- Cleaning up image(s)" >&2; echo
    docker rmi -f ${IMAGE_IDS} || true
fi