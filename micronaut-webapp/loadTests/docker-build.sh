#!/bin/sh
image_name=loadgeneration

docker_file=Dockerfile
quiet=false

if [ "$1" = "-q" ]
then
    quiet=true
    shift
fi

docker build -f $docker_file .  -t $image_name

if [ "${quiet}" = false ]
then
    echo
    echo
    echo "To run the docker container execute:"
    echo "    $ docker run $image_name"
fi
