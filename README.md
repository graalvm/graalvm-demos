# GraalVM Demos

This repository contains a collection of example applications that highlight key features and best practices for working with GraalVM technologies.

## Get Started

To get started, clone this repository and navigate to the relevant demo directory:
```bash
git clone https://github.com/graalvm/graalvm-demos.git
cd graalvm-demos
```

You will find instructions for running a particular demo in the corresponding _README.md_ file. Some demos redirect you to a specific guide on the [GraalVM website](https://www.graalvm.org/latest/guides/).

## Native Image Demos

Example applications showcasing the capabilities of GraalVM Native Image, including performance optimization and configuration tips.

### Build
Demos for building native images, including configurations and setup steps for various use cases.

* [hello-world](native-image/hello-world/) - A HelloWorld example showing how to create a native executable from a class file
* [build-from-jar](native-image/build-from-jar/) - Shows how to create a JAR file without using Maven or Gradle, and build a native executable from that JAR
* [build-java-modules](native-image/build-java-modules/) - Shows how to compile a modularized Java application into a native executable without using Maven or Gradle
* [build-shared-library](native-image/build-shared-library/) - Shows how build a native shared library and then load it from a C application
* [build-static-images](native-image/build-static-images/) - Shows how to create a fully static and a mostly-static native executable, unlike the default dynamic one 
* [build-with-js-embedded](native-image/build-with-js-embedded/) - Shows how to embedded JavaScript into a Java application, and then compile it ahead of time
* [list-files](native-image/list-files/) - Shows how to create a native executable from the command line, and then apply Profile-Guided Optimization (PGO)
* [native-build-tools](native-image/native-build-tools/) - Contains two Java projects, and shows how to create native executables from those applications using [Maven](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html) and [Gradle](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html) plugins for GraalVM Native Image

### Configure
Demos illustrating how to compile applications with Native Image that use some dynamic Java features including reflection, resource access, and so on.

* [access-environment-variables](native-image/access-environment-variables/) - Showing how to access environment variables in a native executable at run time
* [configure-with-tracing-agent](native-image/configure-with-tracing-agent/) - Demonstrate how to use the tracing agent to create a native executable that relies on reflection and requires configuration
* [include-metadata](native-image/include-metadata/) - Contains Maven and Gradle Java projects and demonstrates how to include reachability metadata using [Native Build Tools](https://graalvm.github.io/native-build-tools/)
* [include-resources](native-image/include-resources/) - Demonstrates how to register resources to be included in a native executable by providing a resource configuration file
* [specify-class-init](native-image/specify-class-init/) - Demonstrates how to influence the default class initialization policy, and initialize a specific class at build time
* [use-system-properties](native-image/use-system-properties/) - Demonstrates how to use system properties in a native executable at build time versus at run time

### Containerize
Demos focusing on containerizing native Java applications and following best practices.

* [spring-boot-microservice-jibber](native-image/containerize/spring-boot-microservice-jibber/) - Demonstrates how to create a native executable for a Spring Boot web server, containerize it, and run
* [tiny-java-containers](native-image/containerize/tiny-java-containers/) - Shows how a simple Java application and the `jdk.httpserver` module can be compiled to produce small container images

### Monitor
Demos showcasing how to monitor native applications using observability and diagnostics tools.

* [add-jfr](native-image/add-jfr/) - Shows how to build a native executable with JDK Flight Recorder (JFR) events support
* [add-jmx](native-image/add-jmx/) - Shows how to build, run, and interact with a native executable using Java Management Extensions (JMX) 
* [add-logging](native-image/add-logging/) - Demonstrates how add logging to a native executable by providing necessary logging configuration
* [create-heap-dump](native-image/create-heap-dumps/) - Shows how to enable heap dump support and describes all possible ways how to create a heap dump from a native executable
* [embed-sbom](native-image/embed-sbom/) - Demonstrates how to embed an SBOM in a native executable to identify its dependencies

### Optimize
Demos optimizing native applications for different criteria (runtime and performance tuning, file size, build time, and more).

* [emit-build-report](native-image/emit-build-report/) - Shows how to optimize Size of a native executable using Build Reports
* [optimize-memory](native-image/optimize-memory/) - Shows how to optimize memory footprint of a native executable
* [optimize-with-pgo](native-image/optimize-with-pgo/) - Shows how to optimize a native executable with Profile-Guided Optimization (PGO) for performance and throughput

### Benchmark
Performance measurement demos for Native Image.

* [jmh/binary-tree](native-image/benchmark/jmh/binary-tree/) - Shows how to run a Java Microbenchmark Harness (JMH) benchmark as a native executable

### Clouds
Demos showcasing the building and deployment of native applications to Oracle Cloud Infrastructure (OCI), AWS, and Google Cloud.

* [native-aws-fargate](native-image/clouds/native-aws-fargate/) - Shows how to containerize a native Java application and then deploy it using the Amazon Elastic Container Registry and AWS Fargate
* [native-aws-lambda](native-image/clouds/native-aws-lambda/) - Demonstrates how to deploy both Java 17 and Native Image applications onto the AWS Lambda platform
* [native-google-cloud-run](native-image/clouds/native-google-cloud-run/) - Demonstrates how to deploy a native Java application onto the Google Cloud Run platform
* [native-oci-cloud-shell](native-image/clouds/native-oci-cloud-shell/) - Shows how to get started quickly with Oracle GraalVM and use Native Image in Oracle Cloud Infrastructure (OCI) Cloud Shell
* [native-oci-container-instances](native-image/clouds/native-oci-container-instances/) - Shows how to containerize a native Java application and then deploy it on OCI using the Container Instance service
* [native-oci-generative-ai](native-image/clouds/native-oci-generative-ai/) - Demonstrates how to use the OCI Generative AI service provided in a Java application, and then compile it ahead of time with Maven

### Microservices
Demos for building microservices ahead of time using frameworks such as Micronaut and Spring Boot.

* [micronaut-hello-rest-gradle](native-image/microservices/micronaut-hello-rest-gradle) - Demonstrates how to build a native executable from a Micronaut application using [Grade plugin for Native Image](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
* [micronaut-hello-rest-maven](native-image/microservices/micronaut-hello-rest-maven) - Demonstrates how to build a native executable from a Micronaut application using [Maven plugin for Native Image](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html)

## Compiler Demos

Demos designed to test and showcase the capabilities of the Graal Just-In-Time (JIT) compiler.
These examples focus on evaluating the compiler's performance, including its optimizations for modern Java workloads.

* [java-stream-benchmark](compiler/java-stream-benchmark/) - A simple Java Stream benchmark to test the Graal JIT compiler performance against C2

## Graal Languages Demos

You can find demos and guides for [GraalJS](https://www.graalvm.org/javascript/), [GraalPy](https://www.graalvm.org/python/), [GraalWasm](https://www.graalvm.org/webassembly/), and other Graal Languages at [github.com/graalvm/graal-languages-demos/](https://github.com/graalvm/graal-languages-demos/).

## Archived Demos

Legacy or blog-related demos, as well as examples involving polyglot capabilities.

## License

Unless specified otherwise, all code in this repository is licensed under the [Universal Permissive License (UPL)](http://opensource.org/licenses/UPL).