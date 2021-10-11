#!/bin/bash

sudo yum install -y git

# Open ports for the services to communicate
sudo firewall-cmd --zone=public --permanent --add-port=8080-8085/tcp
sudo firewall-cmd --zone=public --permanent --add-port=8443/tcp
sudo firewall-cmd --reload

#git clone https://github.com/graalvm/graalvm-demos
git clone https://github.com/neomatrix369/graalvm-demos
cd graalvm-demos
git checkout provide-Docker-scripts
cd docker-images/ && ./buildDockerImage.sh

echo "|-----------------------------------------------------------------------------------------------------|"
echo "|                                                                                                     |"
echo "| ssh onto the OCI box followed by the below command:                                                 |"
echo "|                                                                                                     |"
echo "|       $ ./run-docker-container-in-the-cloud.sh                                                      |"
echo "|                                                                                                     |"
echo "|-----------------------------------------------------------------------------------------------------|"