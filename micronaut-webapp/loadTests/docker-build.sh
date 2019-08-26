#!/bin/sh
image_name=loadgeneration

docker_file=Dockerfile

docker build -f $docker_file .  -t $image_name
echo
echo
echo "To run the docker container execute:"
echo "    $ docker run $image_name"
