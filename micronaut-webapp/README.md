# GraalVM Micronaut Todo Webapp

## Summary

The Todo web application is an example application built with
[Micronaut](https://micronaut.io/) and GraalVM in the form of microservices
architecture. The purpose of the application is to demonstrate GraalVM and
Native Image speedup in comparison with Vanilla JVM.

## Project Structure

The application is currently broken into 3 pieces. The `frontend`,
`todo-service` and `loadTest` project. Additionally, the `api` project contains
classes shared amongst the `frontend` and the `todo-service`.

### Frontend

A minimalistic server-side rendering web application that renders data from the
`todo-service`. The `frontend` communicates to the backend service via a http
client. Its sole purpose(for now) is to render data.

### Todo-service

A backend web service that exposes RESTful CRUD operations on Todo items. This
service is currently backed by a in-memory data structure, however new
implementations can be easily added.

### Api

Common classes shared by both the `frontend` and the `todo-service`.

### Load tests

A project containing JMeter load tests. Currently, the load tests are applied on the `todo-service` only.

## Building

### Pre-requisites
- Gradle
- Docker
- Docker Compose
- GraalVM (if run without Docker)
- JMeter (for running load tests, if run without Docker)

There are generally two ways build and run the project: via Docker and via Kubernetes.

### Gradle

Build all the services:
```
./gradlew assemble
```

If you want to build a specific service, run:
```
./gradlew frontend:assemble //for the frontend
./gradlew todo-service:assemble //for the todo-service
```
Execute the services:
```
./gradlew todo-service:run //for the todo-service
./gradlew frontend:run //for the frontend
```
The above will start the `frontend` service in [http://localhost:8081/todos](http://localhost:8081/todos) and the `todo-service` in [https://localhost:8443/api/todos](https://localhost:8443/api/todos).

To run load tests you can execute the below. If your are running without docker
make sure you have JMeter installed and it is on the `PATH`:

```
cd loadTests && ./start-load-tests.sh
```

Open `loadTests/out/index.html` to see the results.


### Docker
To build and run all the service including load tests via Docker:

- `./gradlew assemble`
- `./deployments/local/build-docker-compose.sh [|graalvm|hotspot]` -- this command builds a Docker composed file with different Docker images running on either: Native Image, GraalVM or Hotspot.
- `docker-compose up --build`

### Kubernetes
To build and run all the service including load tests via Kubernetes:

- `./gradlew assemble`
- `./deployments/k8/build-kubernetes.sh [|graalvm|hotspot]` -- this command builds Docker images, if they do not exist, and creates a Kubernetes configuration files in `k8` folder.
- [Optional] For cluster deployment the created images must be pushed into Docker registry used by the cluster and `imagePullSecrets` may be required.
- `kubectl create -f k8`
