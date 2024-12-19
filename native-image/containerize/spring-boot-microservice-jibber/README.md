# Containerize a Spring Boot 3 Native Microservice and Run in a Container

You can find the steps to run this demo on [the website](https://www.graalvm.org/latest/reference-manual/native-image/guides/containerise-native-executable-and-run-in-docker-container/).

## Measure the Performance of the Application and Metrics

The Spring Actuator dependency is added to the project, along with support for Prometheus.
If you want to test the performance of either the JVM version, or the native executable version of the application, you can make use of the Prometheus support.
If you are hosting the application locally, it is available on port 8080:

[http://localhost:8080/actuator/prometheus](http://localhost:8080/actuator/prometheus)