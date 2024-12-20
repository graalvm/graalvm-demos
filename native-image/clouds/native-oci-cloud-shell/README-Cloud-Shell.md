# Java Hello World with Oracle GraalVM in OCI Cloud Shell

This example shows how you can get started quickly with Oracle GraalVM in Oracle Cloud Infrastructure (OCI) Cloud Shell. This example uses a simple hello world Java application built with Oracle GraalVM Native Image and JDK (Java Development Kit).

## What is GraalVM?

[GraalVM](https://www.oracle.com/in/java/graalvm/) is a high-performance JDK distribution that accelerates Java workloads. GraalVM Native Image ahead-of-time compilation builds your Java application into a native executable that is small, starts fast, and uses less memory and CPU. Leading Java microservices frameworks such as Spring Boot, Micronaut, Quarkus and Helidon support GraalVM Native Image.

Oracle GraalVM is available for use on Oracle Cloud Infrastructure (OCI) at no additional cost.

## What is Cloud Shell?

[Cloud Shell](https://www.oracle.com/devops/cloud-shell/) is a free-to-use browser-based terminal accessible from the Oracle Cloud Console. It provides access to a Linux shell with preinstalled developer tools and a pre-authenticated OCI CLI. You can use the shell to interact with OCI resources, follow labs and tutorials, and quickly run utility commands.

Oracle GraalVM for JDK 17 (with Native Image) is preinstalled in Cloud Shell, so you don’t have to install and configure a development machine to get started.

## Step 1: Launch Cloud Shell

1. [Login to OCI Console and launch Cloud Shell](https://cloud.oracle.com/?bdcstate=maximized&cloudshell=true).

## Step 2: Select GraalVM as the current JDK

1. List the installed JDKs:

    ```shell
    csruntimectl java list
    ```

    The output should be similar to:

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
    git init graalvm-native-oci-cloud-shell

    cd graalvm-native-oci-cloud-shell

    git remote add origin https://github.com/graalvm/graalvm-demos.git

    git config core.sparsecheckout true

    echo "clouds/native-oci-cloud-shell/*">>.git/info/sparse-checkout

    git pull --depth=1 origin master

    cd clouds/native-oci-cloud-shell
    ```

2. [OPTIONAL] Confirm software version and environment variables.

    [Check software version and environment variables](README-check-version-env-vars.md)

3. Build a JAR file for the example app.

    ```shell
    ./mvnw clean package
    ```

4. Run the JAR:

    ```shell
    java -jar target/my-app-1.0-SNAPSHOT.jar
    ```

    The output should be similar to:

    ```text
    Hello World!
    ```

## Step 4: Build and run a native executable

Use GraalVM Native Image to produce a native executable.

**Option 1: Quick Build enabled**

You will notice the `Quick Build` mode reduces the time required to generate a native executable, making it convenient to use Native Image builds in a typical development cycle (edit, compile, test, and debug). The `Quick Build` mode is recommended for development purposes only.

1. To enable `Quick Build`, open [pom.xml](./pom.xml) in a text editor such as Nano and uncomment the line shown:

    ```xml
    <quickBuild>true</quickBuild>
    ```

2. Use the Native Image maven plugin to create a native executable:

    ```shell
    ./mvnw clean -Pnative -DskipTests package
    ```

3. Run the native executable using:

    ```shell
    ./target/my-app
    ```

    The output should be similar to:

    ```text
    Hello World!
    ```


**Option 2: Quick Build disabled**

1. To disable `Quick Build`, open [pom.xml](pom.xml) in a text editor such as Nano and comment the line shown:

    ```xml
    <!-- <quickBuild>true</quickBuild> -->
    ```

2. Use the Native Image maven plugin to create a native executable:

    ```shell
    ./mvnw clean -Pnative -DskipTests package
    ```

3. Run the native executable using:

    ```shell
    ./target/my-app
    ```

    The output should be similar to:

    ```text
    Hello World!
    ```

Learn how to start using Oracle GraalVM with other OCI services at [docs.oracle.com](https://docs.oracle.com/en/graalvm/jdk/23/docs/getting-started/oci/).