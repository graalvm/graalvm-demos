#!/bin/sh
image_name=frontend-native
docker_file=Dockerfile
quiet=false

if [ "$1" = "-q" ]
then
    quiet=true
    shift
fi

case "$1" in
    graalvm* )
    image_name=frontend-graalvm
    docker_file=Dockerfile-graalvm
    ;;
    hotspot*)
    image_name=frontend-hotspot
    docker_file=Dockerfile-hotspot
    ;;
esac

docker build -f $docker_file .  -t $image_name

if [ "${quiet}" = false ]
then
    echo
    echo
    echo "To run the docker container execute:"
    echo "    $ docker run -p 8080:8080 $image_name"
fi
