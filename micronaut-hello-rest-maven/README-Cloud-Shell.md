# Micronaut Hello World REST App with Oracle GraalVM in OCI Cloud Shell

This example shows how you can get started quickly with Oracle GraalVM in Oracle Cloud Infrastructure (OCI) Cloud Shell. This example uses a simple hello world REST application built with the Micronaut framework and GraalVM.

## What is GraalVM?

[GraalVM](https://www.oracle.com/in/java/graalvm/) is an advanced JDK with ahead-of-time Native Image compilation. GraalVM Native Image ahead-of-time compilation builds your Java application into a native executable that is small, starts fast, and uses less memory and CPU. Leading Java microservices frameworks such as Spring Boot, Micronaut, Quarkus and Helidon support GraalVM Native Image.

Oracle GraalVM is available for use on Oracle Cloud Infrastructure (OCI) at no additional cost.

## What is Micronaut?

[Micronaut](https://micronaut.io/) is a modern, JVM-based framework to build modular, easily testable microservice and serverless applications. By avoiding runtime reflection in favor of annotation processing, Micronaut improves the Java-based development experience by detecting errors at compile time instead of runtime, and improves Java-based application start time and memory footprint. Micronaut includes a persistence framework called Micronaut Data that precomputes your SQL queries at compilation time making it a great fit for working with databases like MySQL, Oracle Autonomous Database, etc.

Micronaut uses GraalVM Native Image to build lightweight Java applications that use less memory and CPUs, are smaller and faster because of GraalVM's advanced ahead-of-time compilation technology.

## What is Cloud Shell?

[Cloud Shell](https://www.oracle.com/devops/cloud-shell/) is a free-to-use browser-based terminal accessible from the Oracle Cloud Console. It provides access to a Linux shell with preinstalled developer tools and a preauthenticated OCI CLI. You can use the shell to interact with OCI resources, follow labs and tutorials, and quickly run utility commands.

Oracle GraalVM for JDK 17 (with Native Image) is preinstalled in Cloud Shell, so you don’t have to install and configure a development machine to get started.

## Step 1: Launch Cloud Shell

1. [Login to OCI Console and launch Cloud Shell](https://cloud.oracle.com/?bdcstate=maximized&cloudshell=true).

## Step 2: Select GraalVM as the current JDK

1. List the installed JDKs:

    ```shell
    csruntimectl java list
    ```

    The output should be similar to (versions may vary):

    ```shell
      graalvmjdk-17                                                      /usr/lib64/graalvm/graalvm-java17
    * oraclejdk-11                                                                   /usr/java/jdk-11.0.17
      oraclejdk-1.8                                                        /usr/lib/jvm/jdk-1.8-oracle-x64
    ```

2. Select GraalVM as the current JDK:

    ```shell
    csruntimectl java set graalvmjdk-17
    ```

    The output should be similar to:

    ```shell
    The current managed java version is set to graalvmjdk-17.
    ```

## Step 3: Set up your project, build and run as a JAR

1. Git clone this example.

    ```shell
    git init graalvm-mn-hello-rest-maven

    cd graalvm-mn-hello-rest-maven

    git remote add origin https://github.com/graalvm/graalvm-demos.git

    git config core.sparsecheckout true

    echo "micronaut-hello-rest-maven/*">>.git/info/sparse-checkout

    git pull --depth=1 origin master

    cd micronaut-hello-rest-maven

    ```

2. [OPTIONAL] Confirm software version and environment variables.

    [Check software version and environment variables](../_common/README-check-version-env-vars.md)

3. Build a JAR file for the example app

    ```shell
    ./mvnw package
    ```

4. Run the JAR in the background

    ```shell
    java -jar target/MnHelloRest-0.1.jar &
    ```

5. Test the JAR

    5.1) Should output "Hello World"

    ```shell
    curl http://localhost:8080/
    ```

    5.2) Should output "Hello Micronaut"

    ```shell
    curl http://localhost:8080/Micronaut
    ```

6. Bring the running JAR in the foreground

    ```shell
    fg
    ```

7. Once the app is running in the foreground, press CTRL+C to stop it.

## Step 4: Build and run a native executable

Use GraalVM Native Image to produce a native executable.

1. Build the app native executable

    ```shell
    ./mvnw package -Dpackaging=native-image
    ```

2. Run the app native executable in the background

    ```shell
    ./target/MnHelloRest &
    ```

3. Test the app native executable

    3.1) Should output "Hello World"

    ```shell
    curl http://localhost:8080/
    ```

    3.2) Should output "Hello Micronaut-Graal-Native"

    ```shell
    curl http://localhost:8080/Micronaut-Graal-Native
    ```

4. Bring the running app JAR in the foreground

    ```shell
    fg
    ```

5. Once the app is running in the foreground, press CTRL+C to stop it.
