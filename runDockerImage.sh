#!/bin/bash

set -e
set -u
set -o pipefail

DOCKER_USER_NAME="${DOCKER_USER_NAME:-neomatrix369}"
BASE_GRAALVM_VERSION="21.2.0"
GRAALVM_JDK_VERSION="java11"
# See https://github.com/graalvm/container/pkgs/container/graalvm-ce for all tags related info
DEFAULT_GRAALVM_VERSION="${GRAALVM_JDK_VERSION}-${BASE_GRAALVM_VERSION}"
DOCKER_IMAGE_TAGS_WEBSITE="https://github.com/graalvm/container/pkgs/container/graalvm-ce"

FULL_GRAALVM_VERSION="${1:-"${DEFAULT_GRAALVM_VERSION}"}"
FULL_DOCKER_TAG_NAME="graalvm-demos"
GRAALVM_HOME_FOLDER="/opt/graalvm"
WORKDIR="/graalvm-demos"
SHARED_FOLDER="${PWD}/shared"
DEMO_TYPE="${DEMO_TYPE:-console}"

# Check if the 'graalvm-demos' image version tag exists, else print additional steps information
TARGET_IMAGE="${DOCKER_USER_NAME}/${FULL_DOCKER_TAG_NAME}:${FULL_GRAALVM_VERSION}"
ENTRYPOINT="--entrypoint /bin/bash"
if [[ "${DEMO_TYPE}" == "gui" ]]; then
    VNC_SERVER_ADDRESS="localhost:5900 or 127.0.0.1:5900"
    TARGET_IMAGE="${DOCKER_USER_NAME}/${FULL_DOCKER_TAG_NAME}-${DEMO_TYPE}:${FULL_GRAALVM_VERSION}"
    ENTRYPOINT=" "
    echo "Also loading a X11VNC server, use any VNCViewer client(s) to log onto ${VNC_SERVER_ADDRESS} to access the GUI apps running in this container." >&2;  echo ""
fi
CONTAINER_NAME="$(echo ${TARGET_IMAGE} | tr '/:' '-')"

echo "Attempting to download the ${TARGET_IMAGE} docker image from local (or remote) repos, this may take a moment..."  >&2
IMAGE_EXISTS=$(docker pull ${TARGET_IMAGE} >&2 || (echo $? && true))

if [[ ! -n "${IMAGE_EXISTS}" ]]; then
    echo ${IMAGE_EXISTS} >&2
    echo ""; echo "The \"${TARGET_IMAGE}\" Docker image found in the Docker registry." >&2; echo ""
else
    echo "" >&2
    echo "The \"${TARGET_IMAGE}\" Docker image (with the version tag) does not exist in the local (or remote) Docker registry." >&2
    echo "" >&2
    echo "A valid version tag name would like this: 'java11-21.2.0'. Please check ${DOCKER_IMAGE_TAGS_WEBSITE}, to verify if this tag is valid and exists, or pick a valid one from there." >&2
    echo "" >&2
    echo "If it is valid, then please try to build it using the below commands:" >&2
    echo "" >&2
    echo "   $ cd docker-images" >&2
    echo "   $ ./buildDockerImages.sh \"${FULL_GRAALVM_VERSION}\"" >&2
    echo "   $ cd .." >&2
    echo "" >&2
    echo "And then attempt to re-run the image using this command (from the root directory of the project):" >&2
    echo "   " >&2
    echo "   $ ./runDockerImages.sh \"${FULL_GRAALVM_VERSION}\"" >&2
    echo "   " >&2
    exit 1
fi

# By doing this we are extracting away the .m2 and .gradle folders from inside the
# container to the outside, giving us a few advantages: speed gains, reduced image size,
# among others
M2_INSIDE_CONTAINER="/root/.m2/"
M2_ON_HOST="${SHARED_FOLDER}/_dot_m2_folder"
echo; echo "-- Creating/using folder '${M2_ON_HOST}' mounted as '.m2' folder inside the container (${M2_INSIDE_CONTAINER})" >&2
mkdir -p ${M2_ON_HOST}

GRADLE_REPO_INSIDE_CONTAINER="/root/.gradle/"
GRADLE_REPO_ON_HOST="${SHARED_FOLDER}/_dot_gradle_folder"
echo; echo "-- Creating/using folder '${GRADLE_REPO_ON_HOST}' mounted as '.gradle' folder inside the container (${GRADLE_REPO_INSIDE_CONTAINER})" >&2
mkdir -p ${GRADLE_REPO_ON_HOST}

SBT_REPO_INSIDE_CONTAINER="/root/.sbt/"
SBT_REPO_ON_HOST="${SHARED_FOLDER}/_dot_sbt_folder"
echo; echo "-- Creating/using folder '${SBT_REPO_ON_HOST}' mounted as '.sbt' folder inside the container (${SBT_REPO_INSIDE_CONTAINER})" >&2
mkdir -p ${SBT_REPO_ON_HOST}

IVY_REPO_INSIDE_CONTAINER="/root/.ivy2/"
IVY_REPO_ON_HOST="${SHARED_FOLDER}/_dot_ivy_folder"
echo; echo "-- Creating/using folder '${IVY_REPO_ON_HOST}' mounted as '.ivy*' folder inside the container (${IVY_REPO_INSIDE_CONTAINER})" >&2
mkdir -p ${IVY_REPO_ON_HOST}

echo; echo "--- Running Docker image (${DEMO_TYPE} mode) for GraalVM version ${FULL_GRAALVM_VERSION} for ${WORKDIR}";  >&2
echo "Loading volumes, this will take a moment or two." >&2; echo "";

docker run --interactive --tty  --rm                                          \
            --name "${CONTAINER_NAME}"                                        \
	        --volume $(pwd):${WORKDIR}                                        \
        	--volume "${M2_ON_HOST}":${M2_INSIDE_CONTAINER}                   \
            --volume "${GRADLE_REPO_ON_HOST}":${GRADLE_REPO_INSIDE_CONTAINER} \
            --volume "${SBT_REPO_ON_HOST}":${SBT_REPO_INSIDE_CONTAINER}       \
            --volume "${IVY_REPO_ON_HOST}":${IVY_REPO_INSIDE_CONTAINER}       \
        	--workdir ${WORKDIR}                                              \
        	--env JAVA_HOME=${GRAALVM_HOME_FOLDER}                            \
            -p 3000:3000                                                      \
            -p 5900:5900                                                      \
            -p 5901:5901                                                      \
            -p 5902:5902                                                      \
        	-p 8080:8080                                                      \
            -p 8081:8081                                                      \
        	-p 8088:8088                                                      \
            -p 8443:8443                                                      \
        	-p 8888:8888                                                      \
            ${ENTRYPOINT}                                                     \
        	"${TARGET_IMAGE}"