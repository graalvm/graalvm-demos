# Deploying the sample service with Kubernetes

## Create Kubernetes scripts
From the example root directory run:
- `./gradlew assemble`
- `./deployments/k8/build-kubernetes.sh [|native|graalvm|hotspot]` If not argument is given the service will be built with native-image

The kubernetes scripts supports docker repositories, pass the repository as parameter to the script eg:
- `./deployments/k8/build-kubernetes.sh native gcr.io` If not argument is given the service will be built with native-image

### Building and Deploying EE versions

- To build EE version of the service call the build script with the env var `GRAALVM_DOCKER-EE` set to a docker container containing the `EE` version of graalvm eg:
 `GRAALVM_DOCKER_EE=graal-ee:19.2.0.1 ./deployments/k8/build-kubernetes.sh native`
