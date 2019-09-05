# GraalVM Micronaut Todo webapp

## Summary

The Todo web application is an example application built with Micronaut and GraalVM in the form of a microservice architecture. 

The purpouse of the application is to demonstrate GraalVM and Native Image speed ups in comparison with vanilla jvm.


## Project Structure

The application is currently broken into 3 pieces. The `frontend`, `todo-service` and `loadTest` project. Aditionally the `api`
project contains classes shared amongst the `frontend` and the `todo-service`

### Frontend

A minimalistic service side rendering web application that renders data from the `todo-service`. The `frontend` comunincates to the backend service via a http client.
It sole purpouse(for now) is to render data.

### Todo-service

A backend webservice that exposes RESTful CRUD operations on Todo items. This service is currently backed by a in-memory data structure, however new implementations can be easily added.

### Api

Common classes shared by both the `frontend` and the `Todo-service`

### Load tests

A project containing jmeter load tests. Currently the load test apply traffic on the `todo-service` only


## Building

### Pre-requisites
- Gradle
- Docker
- Docker Compose
- GraalVM (if run without docker)
- JMeter (for running load tests, if run without docker)

There are generally two ways build and run the project 

### Gradle
To build all the services

```
./gradlew assemble
```

If you want to build an specific service
```
./gradlew frontend:assemble //for the frontend
./gradlew todo-service:assemble //for the todo-service
```

To execute the services
```
./gradlew todo-service:run //for the todo-service
./gradlew frontend:run //for the frontend
```
The above will start the `frontend` service in [http://localhost:8081/todos](http://localhost:8081/todos) and the `todo-service` in [https://localhost:8443/api/todos](https://localhost:8443/api/todos)


To run load tests you can execute the below. If your are running without docker make sure you have jmeter install and it is in your `PATH`

```
cd loadTests && ./start-load-tests.sh
```

Open `loadTests/out/index.html` to see the results


### Docker
To build and run all the service including load tests via docker:

- `./gradlew assemble`
- `./build-docker-compose.sh [|graalvm|hotspot]` Builds a docker compose file with different docker images running on either: native-image, graalvm or hotspot
- `docker-compose up --build`

### Kubernetes
To build and run all the service including load tests via kubernetes:

- `./gradlew assemble`
- `./build-kubernetes.sh [|graalvm|hotspot]` Builds Docker images, if they don't exist, and creates a kubernetes config files in `k8` folder.
- [Optional] for cluster deployment the created images must be pushed into Docker registry used by the cluster and `imagePullSecrets` may be required.
- `kubectl create -f k8`


