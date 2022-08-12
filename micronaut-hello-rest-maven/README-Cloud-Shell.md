# Micronaut Hello World REST App with GraalVM Enterprise in OCI Cloud Shell

This example shows how you can get started quickly with GraalVM Enterprise Edition in Oracle Cloud Infrastructre (OCI) Cloud Shell. This example uses a simple hello world REST application built with the Micronaut framework and GraalVM Enterprise Native Image and JDK (Java Development Kit).

## What is GraalVM?

[GraalVM](https://www.oracle.com/in/java/graalvm/) is a high-performance JDK distribution that accelerates Java workloads. GraalVM Native Image ahead-of-time compilation builds your Java application into a native executable that is small, starts fast, and uses less memory and CPU. Leading Java microservices frameworks such as Spring Boot, Micronaut, Quarkus and Helidon support GraalVM Native Image.

GraalVM Enterprise Edition is available for use on Oracle Cloud Infrastructure (OCI) at no additional cost.

## What is Micronaut?

[Micronaut](https://micronaut.io/) is a modern, JVM-based framework to build modular, easily testable microservice and serverless applications. By avoiding runtime reflection in favor of annotation processing, Micronaut improves the Java-based development experience by detecting errors at compile time instead of runtime, and improves Java-based application start time and memory footprint. Micronaut includes a persistence framework called Micronaut Data that precomputes your SQL queries at compilation time making it a great fit for working with databases like MySQL, Oracle Autonomous Database, etc.

Micronaut uses GraalVM Native Image to build lightweight Java applications that use less memory and CPUs, are smaller and faster because of an advanced ahead-of-time compilation technology.

## What is Cloud Shell?

[Cloud Shell](https://www.oracle.com/devops/cloud-shell/) is a free-to-use browser-based terminal accessible from the Oracle Cloud Console. It provides access to a Linux shell with preinstalled developer tools and a preauthenticated OCI CLI. You can use the shell to interact with OCI resources, follow labs and tutorials, and quickly run utility commands.

GraalVM Enterprise JDK 17 and Native Image are preinstalled in Cloud Shell, so you don’t have to install and configure a development machine to get started.

## Step 1: Launch Cloud Shell 

1. [Login to OCI Console and launch Cloud Shell](https://cloud.oracle.com/?bdcstate=maximized&cloudshell=true).

## Step 2: Select GraalVM as the current JDK 

1. List the installed JDKs:

    ```shell
    csruntimectl java list
    ```

    The output should be similar to:

    ```shell
      graalvmeejdk-17.0.4                                    /usr/lib64/graalvm/graalvm22-ee-java17
    * openjdk-11.0.15                   /usr/lib/jvm/java-11-openjdk-11.0.15.0.9-2.0.1.el7_9.x86_64
      openjdk-1.8.0.332                /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.332.b09-1.el7_9.x86_64
    ```

2. Select GraalVM as the current JDK:

    ```shell
    csruntimectl java set graalvmeejdk-17.0.4
    ```

    The output should be similar to:

    ```shell
    The current managed java version is set to graalvmeejdk-17.0.4.
    ```

## Step 3: [OPTIONAL] Confirm software version and environment variables

This step is optional - [Check software version and environment variables](../_common/README-check-version-env-vars.md)


## Step 4: Set up your project, build and run as a JAR

1. Git clone this example.

    ```shell
    git init graalvmee-micronaut-hello-rest-maven

    cd graalvmee-micronaut-hello-rest-maven

    git remote add origin https://github.com/graalvm/graalvm-demos.git

    git config core.sparsecheckout true

    echo "micronaut-hello-rest-maven/*">>.git/info/sparse-checkout

    git pull --depth=1 origin master

    cd micronaut-hello-rest-maven

    ```

2. Build a JAR file for the example app

    ```shell
    mvn package
    ```

    **OR** 

    ```shell
    ./mvnw package
    ```

3. Run the JAR in the background

    ```shell
    java -jar target/MnHelloRest-0.1.jar &
    ```

4. Test the JAR

    4.1) Should output "Hello World"

    ```shell
    curl http://localhost:8080/
    ```

    4.2) Should output "Hello Micronaut"

    ```shell
    curl http://localhost:8080/Micronaut
    ```

5. Bring the running JAR in the foreground

    ```shell
    fg
    ```

6. Once the app is running in the foreground, press CTRL+C to stop it.


## Step 5: Build and run a native executable

Use GraalVM Native Image to produce a native executable.

1. Build the app native executable

    ```shell
    mvn package -Dpackaging=native-image
    ```

    **OR** 

    ```shell
    ./mvnw package -Dpackaging=native-image
    ```
    
    The output should be similar to:
    
    ```shell
    ...
    You enabled -Ob for this image build. This will configure some optimizations to reduce image build time.
    ...
    ==============================================================================================================
    GraalVM Native Image: Generating 'MnHelloRest' (executable)...
    ==============================================================================================================
    [1/7] Initializing...                                                                         (14.8s @ 0.14GB)
     Version info: 'GraalVM 22.2.0 Java 17 EE'
     Java version info: '17.0.4+11-LTS-jvmci-22.2-b05'
     C compiler: gcc (redhat, x86_64, 11.2.1)
     Garbage collector: Serial GC
     4 user-specific feature(s)
     - io.micronaut.buffer.netty.NettyFeature
     - io.micronaut.core.graal.ServiceLoaderFeature
     - io.micronaut.http.netty.graal.HttpNettyFeature
     - io.micronaut.jackson.JacksonDatabindFeature
    [2/7] Performing analysis...  [*********]                                                    (134.8s @ 1.36GB)
      13,633 (91.99%) of 14,820 classes reachable
      18,746 (56.91%) of 32,942 fields reachable
      73,576 (63.75%) of 115,405 methods reachable
         753 classes,   342 fields, and 2,788 methods registered for reflection
          63 classes,    68 fields, and    55 methods registered for JNI access
           4 native libraries: dl, pthread, rt, z
    [3/7] Building universe...                                                                    (16.3s @ 1.51GB)
    [4/7] Parsing methods...      [****]                                                          (19.9s @ 1.69GB)
    [5/7] Inlining methods...     [***]                                                            (7.9s @ 1.40GB)
    [6/7] Compiling methods...    [********]                                                      (67.1s @ 2.21GB)
    [7/7] Creating image...                                                                       (12.4s @ 2.23GB)
      19.93MB (47.43%) for code area:    52,033 compilation units
      21.77MB (51.81%) for image heap:  340,838 objects and 292 resources
     327.48KB ( 0.76%) for other data
      42.02MB in total
    --------------------------------------------------------------------------------------------------------------
    Top 10 packages in code area:                          Top 10 object types in image heap:
       1.83MB com.oracle.svm.core.code                        4.60MB byte[] for code metadata
       1.18MB sun.security.ssl                                3.00MB byte[] for java.lang.String
     750.79KB java.util                                       2.93MB java.lang.Class
     465.04KB com.sun.crypto.provider                         2.21MB java.lang.String
     424.19KB java.lang.invoke                                2.06MB byte[] for general heap data
     407.35KB java.text                                     765.40KB byte[] for reflection metadata
     406.78KB io.netty.buffer                               639.05KB com.oracle.svm.core.hub.DynamicHubCompanion
     386.15KB reactor.core.publisher                        407.69KB java.util.concurrent.ConcurrentHashMap$Node
     372.09KB java.lang                                     392.31KB c.o.s.core.hub.DynamicHub$ReflectionMetadata
     364.29KB io.netty.handler.codec.http2                  377.50KB java.util.HashMap$Node
      13.05MB for 501 more packages                           3.64MB for 3063 more object types
    --------------------------------------------------------------------------------------------------------------
                       13.6s (4.8% of total time) in 35 GCs | Peak RSS: 4.14GB | CPU load: 1.77
    --------------------------------------------------------------------------------------------------------------
    Produced artifacts:
     /home/sachin_pik/graalvmee-micronaut-hello-rest-maven/micronaut-hello-rest-maven/target/MnHelloRest (executable)
     /home/sachin_pik/graalvmee-micronaut-hello-rest-maven/micronaut-hello-rest-maven/target/MnHelloRest.build_artifacts.txt (txt)
    ==============================================================================================================
    Finished generating 'MnHelloRest' in 4m 41s.
    ...    
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
