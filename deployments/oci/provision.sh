#!/bin/bash

sudo yum install -y git

# Open ports for the services to communicate
sudo firewall-cmd --zone=public --permanent --add-port=8080-8085/tcp
sudo firewall-cmd --zone=public --permanent --add-port=8443/tcp
sudo firewall-cmd --reload

git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos
git checkout master

echo "|-------------------------------------------------------------------------------------------------------|"
echo "|                                                                                                       |"
echo "| ssh onto the OCI box followed by the below command:                                                   |"
echo "|                                                                                                       |"
echo "|     $ ./run-docker-container-in-the-cloud.sh [CLI ARGS IN DOUBLE QUOTES]                              |"
echo "|                                                                                                       |"
echo "|     [CLI ARGS IN DOUBLE QUOTES] - (optional) can be shell commands or build/run                       |"
echo "|                                   shellscript name and parameter                                      |"
echo "|     No args will give the shell-prompt to allow running the same shell commands                       |"
echo "|                                                                                                       |"
echo "| for e.g.                                                                                              |"
echo "|     $ ./run-docker-container-in-the-cloud.sh                                                          |"
echo "|                                                                                                       |"
echo "|                                       or                                                              |"
echo "|                                                                                                       |"
echo "|     $ ./run-docker-container-in-the-cloud.sh \"cd graalvm-demos; ./buildDockerImage.sh java11-21.2.0\"|"
echo "|                                                                                                       |"
echo "|        see https://github.com/graalvm/graalvm-demos#graalvm-demos for more examples                   |"
echo "|                                                                                                       |"
echo "|-------------------------------------------------------------------------------------------------------|"


echo "|-----------------------------------------------------------------------------------------------------|"
echo "|                                                                                                     |"
echo "|     Once in the box you can run the build or run shellscripts from your local machine,              |"
echo "|     via the command-line interface (CLI). The steps are mentioned on the README page:               |"
echo "|                                                                                                     |"
echo "|                https://github.com/graalvm/graalvm-demos#graalvm-demos                               |"
echo "|                                                                                                     |"
echo "|-----------------------------------------------------------------------------------------------------|"