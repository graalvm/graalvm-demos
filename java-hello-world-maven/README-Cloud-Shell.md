# Java Hello World with GraalVM Enterprise in OCI Cloud Shell

This example shows how you can get started quickly with GraalVM Enterprise Edition in Oracle Cloud Infrastructre (OCI) Cloud Shell. This example uses a simple hello world Java application built with GraalVM Enterprise Native Image and JDK (Java Development Kit).

## What is GraalVM?

[GraalVM](https://www.oracle.com/in/java/graalvm/) is a high-performance JDK distribution that accelerates Java workloads. GraalVM Native Image ahead-of-time compilation builds your Java application into a native executable that is small, starts fast, and uses less memory and CPU. Leading Java microservices frameworks such as Spring Boot, Micronaut, Quarkus and Helidon support GraalVM Native Image.

GraalVM Enterprise Edition is available for use on Oracle Cloud Infrastructure (OCI) at no additional cost.

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
      graalvmeejdk-17.0.4.1                                         /usr/lib64/graalvm/graalvm22-ee-java17
    * openjdk-11.0.16.1                        /usr/lib/jvm/java-11-openjdk-11.0.16.1.1-1.0.1.el7_9.x86_64
      openjdk-1.8.0.345                       /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.345.b01-1.el7_9.x86_64
    ```

2. Select GraalVM as the current JDK:

    ```shell
    csruntimectl java set graalvmeejdk-17.0.4.1
    ```

    The output should be similar to:

    ```shell
    The current managed java version is set to graalvmeejdk-17.0.4.1.
    ```

## Step 3: [OPTIONAL] Confirm software version and environment variables

This step is optional - [Check software version and environment variables](../_common/README-check-version-env-vars.md)


## Step 4: Set up your project, build and run as a JAR

1. Git clone this example.

    ```shell
    git init graalvmee-java-hello-world-maven

    cd graalvmee-java-hello-world-maven

    git remote add origin https://github.com/graalvm/graalvm-demos.git

    git config core.sparsecheckout true

    echo "java-hello-world-maven/*">>.git/info/sparse-checkout

    git pull --depth=1 origin master

    cd java-hello-world-maven

    ```

2. Build a JAR file for the example app.

    ```shell
    mvn clean package
    ```

3. Run the JAR using:

    ```shell
    java -jar target/my-app-1.0-SNAPSHOT.jar
    ```

    The output should be similar to:
    ```
    Hello World!
    ```

## Step 5: Build and run a native executable

Use GraalVM Native Image to produce a native executable.

**Option 1: Quick Build enabled**

You will notice the `Quick Build` mode reduces the time required to generate a native executable, making it convenient to use Native Image builds in a typical development cycle (edit, compile, test, and debug). The `Quick Build` mode is recommended for development purposes only.

1. To enable `Quick Build`, open [pom.xml](./pom.xml) in a text editor such as Nano and uncomment the line shown:

    ```
    <buildArg>-Ob</buildArg>
    ```

2. Use the Native Image maven plugin to create a native executable:

    ```shell
    export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false

    mvn clean -Pnative -DskipTests package

    ```

    With **Quick Build enabled** in the pom.xml, the output should be similar to:

    ```
    ...
    You enabled -Ob for this image build. This will configure some optimizations to reduce image build time.
    ...
    ========================================================================================================================
    GraalVM Native Image: Generating 'my-app' (executable)...
    ========================================================================================================================
    [1/7] Initializing...                                                                                   (10.7s @ 0.21GB)
    Version info: 'GraalVM 22.2.0.1 Java 17 EE'
    Java version info: '17.0.4.1+1-LTS-jvmci-22.2-b08'
    C compiler: gcc (redhat, x86_64, 11.2.1)
    Garbage collector: Serial GC
    [2/7] Performing analysis...  [*****]                                                                   (20.4s @ 0.68GB)
    1,819 (62.34%) of  2,918 classes reachable
    1,662 (46.88%) of  3,545 fields reachable
    7,638 (37.13%) of 20,569 methods reachable
        17 classes,     0 fields, and   254 methods registered for reflection
        49 classes,    32 fields, and    48 methods registered for JNI access
        4 native libraries: dl, pthread, rt, z
    [3/7] Building universe...                                                                               (3.0s @ 0.93GB)
    [4/7] Parsing methods...      [**]                                                                       (2.7s @ 0.36GB)
    [5/7] Inlining methods...     [***]                                                                      (1.9s @ 0.60GB)
    [6/7] Compiling methods...    [***]                                                                     (11.0s @ 0.72GB)
    [7/7] Creating image...                                                                                  (1.9s @ 0.91GB)
    1.78MB (42.86%) for code area:     4,433 compilation units
    2.23MB (53.52%) for image heap:   36,677 objects and 1 resources
    154.55KB ( 3.63%) for other data
    4.16MB in total
    ------------------------------------------------------------------------------------------------------------------------
    Top 10 packages in code area:                               Top 10 object types in image heap:
    208.78KB com.oracle.svm.jni                                 373.80KB byte[] for code metadata
    184.72KB java.lang                                          316.52KB byte[] for java.lang.String
    168.31KB com.oracle.svm.core.code                           267.37KB java.lang.Class
    144.00KB java.util                                          263.25KB java.lang.String
    104.52KB com.oracle.svm.core.genscavenge                    213.14KB byte[] for general heap data
    80.73KB java.util.concurrent                               111.71KB char[]
    64.87KB java.lang.invoke                                    71.05KB com.oracle.svm.core.hub.DynamicHubCompanion
    52.95KB java.math                                           68.35KB byte[] for reflection metadata
    44.58KB com.oracle.svm.jni.functions                        50.34KB c.o.svm.core.hub.DynamicHub$ReflectionMetadata
    40.68KB java.io                                             44.95KB java.util.HashMap$Node[]
    700.72KB for 99 more packages                               346.14KB for 478 more object types
    ------------------------------------------------------------------------------------------------------------------------
                            1.5s (2.7% of total time) in 14 GCs | Peak RSS: 1.65GB | CPU load: 1.77
    ------------------------------------------------------------------------------------------------------------------------
    Produced artifacts:
    /home/user_graal/java-hello-world-maven/target/my-app (executable)
    /home/user_graal/java-hello-world-maven/target/my-app.build_artifacts.txt (txt)
    ========================================================================================================================
    Finished generating 'my-app' in 53.5s.
    ...
    ```

3. Run the native executable using:

    ```shell
    ./target/my-app
    ```

    The output should be similar to:
    ```
    Hello World!
    ```


**Option 2: Quick Build disabled** 
    
1. To disable `Quick Build`, open [pom.xml](pom.xml) in a text editor such as Nano and comment the line shown:  

    ```
    <!-- <buildArg>-Ob</buildArg> -->
    ```

2. Use the Native Image maven plugin to create a native executable:

    ```shell
    export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false

    mvn clean -Pnative -DskipTests package
    
    ```

    With **Quick Build disabled** in the pom.xml, the output should be similar to:

    ```
    ...
    ========================================================================================================================
    GraalVM Native Image: Generating 'my-app' (executable)...
    ========================================================================================================================
    [1/7] Initializing...                                                                                   (10.8s @ 0.23GB)
    Version info: 'GraalVM 22.2.0.1 Java 17 EE'
    Java version info: '17.0.4.1+1-LTS-jvmci-22.2-b08'
    C compiler: gcc (redhat, x86_64, 11.2.1)
    Garbage collector: Serial GC
    [2/7] Performing analysis...  [*****]                                                                   (20.2s @ 0.69GB)
    1,858 (61.54%) of  3,019 classes reachable
    1,698 (47.23%) of  3,595 fields reachable
    7,633 (36.19%) of 21,092 methods reachable
        17 classes,     0 fields, and   254 methods registered for reflection
        49 classes,    32 fields, and    48 methods registered for JNI access
        4 native libraries: dl, pthread, rt, z
    [3/7] Building universe...                                                                               (3.0s @ 0.95GB)
    [4/7] Parsing methods...      [**]                                                                       (2.9s @ 0.39GB)
    [5/7] Inlining methods...     [***]                                                                      (1.5s @ 0.64GB)
    [6/7] Compiling methods...    [******]                                                                  (44.7s @ 2.12GB)
    [7/7] Creating image...                                                                                  (2.1s @ 2.34GB)
    2.50MB (49.19%) for code area:     3,714 compilation units
    2.43MB (47.88%) for image heap:   36,993 objects and 1 resources
    152.23KB ( 2.92%) for other data
    5.08MB in total
    ------------------------------------------------------------------------------------------------------------------------
    Top 10 packages in code area:                               Top 10 object types in image heap:
    299.87KB java.lang                                          508.50KB byte[] for code metadata
    193.82KB com.oracle.svm.jni                                 319.17KB byte[] for java.lang.String
    190.77KB java.util                                          272.45KB java.lang.Class
    155.80KB com.oracle.svm.core.code                           265.22KB java.lang.String
    118.87KB com.oracle.svm.core.genscavenge                    214.75KB byte[] for general heap data
    111.79KB java.util.concurrent                               111.71KB char[]
    75.68KB java.lang.invoke                                    72.58KB com.oracle.svm.core.hub.DynamicHubCompanion
    73.33KB java.math                                           69.43KB byte[] for reflection metadata
    71.31KB jdk.proxy4                                          51.22KB c.o.svm.core.hub.DynamicHub$ReflectionMetadata
    68.17KB com.oracle.svm.core                                 44.95KB java.util.HashMap$Node[]
    1.15MB for 96 more packages                               348.99KB for 474 more object types
    ------------------------------------------------------------------------------------------------------------------------
                            1.5s (1.7% of total time) in 17 GCs | Peak RSS: 2.94GB | CPU load: 1.76
    ------------------------------------------------------------------------------------------------------------------------
    Produced artifacts:
    /home/user_graal/java-hello-world-maven/target/my-app (executable)
    /home/user_graal/java-hello-world-maven/target/my-app.build_artifacts.txt (txt)
    ========================================================================================================================
    Finished generating 'my-app' in 1m 28s.
    ...
    ```

3. Run the native executable using:

    ```shell
    ./target/my-app
    ```

    The output should be similar to:
    ```
    Hello World!
    ```
