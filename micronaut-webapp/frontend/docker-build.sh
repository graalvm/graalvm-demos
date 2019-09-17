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
    ;;
    hotspot*)
    docker_file=Dockerfile-hotspot
    tag=:openjdk8
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
