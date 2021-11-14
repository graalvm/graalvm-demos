#!/bin/bash

set -eu
set -o pipefail

INSTANCE_PUBLIC_IP="$(get-instance-public-ip.sh)"

echo "Public IP address of the cloud instance running is ${INSTANCE_PUBLIC_IP}"

exit_code=0
ssh opc@${INSTANCE_PUBLIC_IP} \
    'cd graalvm-demos; git checkout provide-Docker-scripts; ./runDockerImage.sh' \
    || exit_code=$? && true

if [[ ${exit_code} -eq 0 ]]; then
	echo ""
	echo "Finished loading up the docker container in the cloud instance."
	echo ""
else
	echo ""
	echo "Failed trying to run the docker container, its possible it has already been executed, check the error logs above."
	echo "Or try to ssh into the container and investigate."
	echo ""
fi

echo "If the container has run successfully (without any errors in the console), then you can ssh into it to gain access to the docker instance."
