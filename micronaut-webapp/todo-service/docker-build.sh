#!/bin/sh
image_name=micronaut-webapp_todo-service
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
    tag=:openjdk8
    docker_file=Dockerfile-hotspot
    ;;
    *)
    ## GRAALVM_DOCKER_EE contains the tag of the docker container with native image ee
    if [[ $GRAALVM_DOCKER_EE != "" ]]
    then
        echo Building native ee image
        tag=:native-ee
        sed s"/oracle\/.*/$GRAALVM_DOCKER_EE as graalvm/g" $docker_file | \
        sed s"/\/opt\/graalvm-ce-graalvm-ce-21.0.0.2/\/usr\/lib64\/graalvm\/graalvm21-ee/g" | \
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
    echo "    $ docker run -p 8443:8443 $image_name$tag"
fi
