#!/bin/sh
image_name=micronaut-webapp_frontend
docker_file=Dockerfile
tag=:native
quiet=false

if [ "$1" = "-q" ]
then
    quiet=true
    shift
fi

case "$1" in
    graalvm* )
    tag=:graalvm-ce
    docker_file=Dockerfile-graalvm
    if [[ $GRAALVM_DOCKER_EE != "" ]]
    then
        echo Building graalvm-ee image
        tag=:graalvm-ee
        sed s"/oracle\/.*/$GRAALVM_DOCKER_EE as graalvm/g" $docker_file  > Docker-graalvm-ee
        docker_file=Docker-graalvm-ee
    fi
    ;;
    hotspot*)
    docker_file=Dockerfile-hotspot
    tag=:openjdk8
    ;;
    *)
    ## GRAALVM_DOCKER_EE contains the tag of the docker container with native image ee
    if [[ $GRAALVM_DOCKER_EE != "" ]]
    then
        echo Building native ee image
        tag=:native-ee
        sed s"/oracle\/.*/$GRAALVM_DOCKER_EE as graalvm/g" $docker_file | \
        sed s"/ARG GRAAL_VERSION=.*/ARG GRAAL_VERSION=graalvm-ee-19.2.0.1/g" | \
        sed  "/^RUN gu*/d"  > Docker-native-ee
        docker_file=Docker-native-ee
    fi
    ;;
esac

docker build -f $docker_file .  -t $image_name$tag

if [ "${quiet}" = false ]
then
    echo
    echo
    echo "To run the docker container execute:"
    echo "    $ docker run -p 8080:8080 $image_name$tag"
fi
