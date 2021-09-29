#!/bin/bash

set -e
set -u
set -o pipefail

DOCKER_USER_NAME=${DOCKER_USER_NAME:-"neomatrix369"}

IMAGE_NAME=${IMAGE_NAME:-graalvm}
IMAGE_VERSION=${IMAGE_VERSION:-rc19}
DOCKER_FULL_TAG_NAME="${DOCKER_USER_NAME}/${IMAGE_NAME}"

WORKDIR=graalvm-demos

docker run --rm                         \
            --interactive --tty         \
	        --volume $(pwd):/${WORKDIR} \
        	-p 8888:8888                \
        	--workdir /${WORKDIR}       \
        	--entrypoint /bin/bash      \
        	${DOCKER_FULL_TAG_NAME}:${IMAGE_VERSION}