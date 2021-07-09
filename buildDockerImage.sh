#!/bin/bash

set -e
set -u
set -o pipefail

if [[ -z ${DOCKER_USER_NAME:-""} ]]; then
  read -p "Docker username (must exist on Docker Hub): " DOCKER_USER_NAME
fi

GRAALVM_VERSION=${GRAALVM_VERSION:-rc19}
IMAGE_NAME=${IMAGE_NAME:-graalvm}
IMAGE_VERSION=${IMAGE_VERSION:-${GRAALVM_VERSION}}
DOCKER_FULL_TAG_NAME="${DOCKER_USER_NAME}/${IMAGE_NAME}"

time docker build -t ${DOCKER_FULL_TAG_NAME}:${IMAGE_VERSION} .