# Java Hello World with GraalVM Enterprise in OCI Cloud Shell

This sample shows how you can get started quickly with GraalVM Enterprise Edition in Oracle Cloud Infrastructre (OCI) Cloud Shell. This sample uses a simple hello world Java application built with GraalVM Enterprise Native Image and JDK (Java Development Kit).

## What is GraalVM?

GraalVM is a high-performance JDK distribution that can accelerate any Java workload running on the HotSpot JVM.

GraalVM Native Image ahead-of-time compilation enables you to build lightweight Java applications that are smaller, faster, and use less memory and CPU. At build time, GraalVM Native Image analyzes a Java application and its dependencies to identify just what classes, methods, and fields are absolutely necessary and generates optimized machine code for just those elements.

GraalVM Enterprise Edition is available for use on Oracle Cloud Infrastructure (OCI) at no additional cost.

## What is Cloud Shell?

Cloud Shell is a free-to-use browser-based terminal accessible from the Oracle Cloud Console. It provides access to a Linux shell with pre-authenticated OCI CLI and other pre-installed developer tools. You can use the shell to interact with OCI resources, follow labs and tutorials, and quickly run commands. 

GraalVM Enterprise Native Image and JDK 17 are preinstalled in Cloud Shell. 

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

## Step 3: [OPTIONAL] Confirm Software Version and Environment Variables

This step is optional - [Check software version and environment variables](./README-CS-check-version-env-vars.md)


## Step 4: Setup Project and Run

1. Git clone this repo.

2. Build a JAR for the sample app.

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

4. Let's use GraalVM Native Image to produce a native executable.

    4.1) **Option 1: Quick Build enabled**

    To enable `Quick Build`, open [pom.xml](./pom.xml) in a text editor like Nano and uncomment the line shown:

    ```
    <buildArg>-Ob</buildArg>
    ```

    Run the Native Image build to generate a native executable:

    ```shell
    export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false

    mvn clean -Pnative -DskipTests package

    ```

    With **Quick Build enabled** in the pom.xml, the output should be similar to:

    ```
    ...
    You enabled -Ob for this image build. This will configure some optimizations to reduce image build time.
    This feature should only be used during development and never for deployment.
    ================================================================================================
    GraalVM Native Image: Generating 'my-app' (executable)...
    ================================================================================================
    [1/7] Initializing...                                                           (10.4s @ 0.21GB)
    Version info: 'GraalVM 22.2.0 Java 17 EE'
    Java version info: '17.0.4+11-LTS-jvmci-22.2-b05'
    C compiler: gcc (redhat, x86_64, 11.2.1)
    Garbage collector: Serial GC
    [2/7] Performing analysis...  [*****]                                           (18.3s @ 0.72GB)
    1,826 (62.38%) of  2,927 classes reachable
    1,665 (46.89%) of  3,551 fields reachable
    7,652 (37.12%) of 20,613 methods reachable
        21 classes,     0 fields, and   258 methods registered for reflection
        49 classes,    32 fields, and    48 methods registered for JNI access
        4 native libraries: dl, pthread, rt, z
    [3/7] Building universe...                                                       (3.2s @ 0.97GB)
    [4/7] Parsing methods...      [**]                                               (2.8s @ 0.40GB)
    [5/7] Inlining methods...     [***]                                              (1.7s @ 0.66GB)
    [6/7] Compiling methods...    [***]                                             (10.0s @ 0.82GB)
    [7/7] Creating image...                                                          (2.3s @ 1.01GB)
    1.78MB (42.88%) for code area:     4,439 compilation units
    2.23MB (53.52%) for image heap:   36,753 objects and 1 resources
    153.49KB ( 3.60%) for other data
    4.16MB in total
    ------------------------------------------------------------------------------------------------
    Top 10 packages in code area:                               Top 10 object types in image heap:
    208.78KB com.oracle.svm.jni                                 374.04KB byte[] for code metadata
    184.72KB java.lang                                          317.32KB byte[] for java.lang.String
    169.35KB com.oracle.svm.core.code                           268.43KB java.lang.Class
    144.00KB java.util                                          263.72KB java.lang.String
    104.52KB com.oracle.svm.core.genscavenge                    213.43KB byte[] for general heap data
    80.73KB java.util.concurrent                               111.71KB char[]
    64.87KB java.lang.invoke                      71.33KB com.oracle.svm.core.hub.DynamicHubCompanion
    52.95KB java.math                                          68.66KB byte[] for reflection metadata
    44.58KB com.oracle.svm.jni.functions       50.56KB c.o.svm.core.hub.DynamicHub$ReflectionMetadata
    40.69KB java.io                                             45.11KB java.util.HashMap$Node[]
    700.71KB for 99 more packages                               347.71KB for 478 more object types
    ------------------------------------------------------------------------------------------------
                1.1s (2.2% of total time) in 14 GCs | Peak RSS: 1.61GB | CPU load: 1.60
    ------------------------------------------------------------------------------------------------
    Produced artifacts:
    /home/user_graal/gvme-java-hello-world/target/my-app (executable)
    /home/user_graal/gvme-java-hello-world/target/my-app.build_artifacts.txt (txt)
    ================================================================================================
    Finished generating 'my-app' in 50.8s.
    ...
    ```

    4.2) **Option 2: Quick Build disabled** 
    
    To disable `Quick Build`, open [pom.xml](pom.xml) in a text editor like Nano and comment the line shown:  

    ```
    <!-- <buildArg>-Ob</buildArg> -->
    ```

    Run the Native Image build to generate a native executable:

    ```shell
    export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false

    mvn clean -Pnative -DskipTests package
    
    ```

    With **Quick Build disabled** in the pom.xml, the output should be similar to:

    ```
    ...
    ================================================================================================
    GraalVM Native Image: Generating 'my-app' (executable)...
    ================================================================================================
    [1/7] Initializing...                                                           (16.7s @ 0.23GB)
    Version info: 'GraalVM 22.2.0 Java 17 EE'
    Java version info: '17.0.4+11-LTS-jvmci-22.2-b05'
    C compiler: gcc (redhat, x86_64, 11.2.1)
    Garbage collector: Serial GC
    [2/7] Performing analysis...  [*****]                                           (30.8s @ 0.76GB)
    1,865 (61.63%) of  3,026 classes reachable
    1,701 (47.24%) of  3,601 fields reachable
    7,647 (36.18%) of 21,135 methods reachable
        21 classes,     0 fields, and   258 methods registered for reflection
        49 classes,    32 fields, and    48 methods registered for JNI access
        4 native libraries: dl, pthread, rt, z
    [3/7] Building universe...                                                       (5.2s @ 1.03GB)
    [4/7] Parsing methods...      [**]                                               (4.6s @ 0.47GB)
    [5/7] Inlining methods...     [***]                                              (2.8s @ 0.73GB)
    [6/7] Compiling methods...    [********]                                        (72.4s @ 2.42GB)
    [7/7] Creating image...                                                          (3.4s @ 0.28GB)
    2.50MB (49.19%) for code area:     3,718 compilation units
    2.43MB (47.85%) for image heap:   37,067 objects and 1 resources
    154.51KB ( 2.97%) for other data
    5.09MB in total
    ------------------------------------------------------------------------------------------------
    Top 10 packages in code area:                               Top 10 object types in image heap:
    299.90KB java.lang                                          508.60KB byte[] for code metadata
    193.82KB com.oracle.svm.jni                                 319.97KB byte[] for java.lang.String
    190.79KB java.util                                          288.09KB java.lang.Class
    157.50KB com.oracle.svm.core.code                           265.69KB java.lang.String
    118.87KB com.oracle.svm.core.genscavenge                    215.05KB byte[] for general heap data
    111.79KB java.util.concurrent                               111.71KB char[]
    75.69KB java.lang.invoke                      72.85KB com.oracle.svm.core.hub.DynamicHubCompanion
    73.33KB java.math                                          69.74KB byte[] for reflection metadata
    71.31KB jdk.proxy4                         51.44KB c.o.svm.core.hub.DynamicHub$ReflectionMetadata
    68.17KB com.oracle.svm.core                                 45.11KB java.util.HashMap$Node[]
    1.15MB for 96 more packages                               348.00KB for 474 more object types
    ------------------------------------------------------------------------------------------------
                2.2s (1.5% of total time) in 18 GCs | Peak RSS: 2.97GB | CPU load: 0.99
    ------------------------------------------------------------------------------------------------
    Produced artifacts:
    /home/user_graal/gvme-java-hello-world/target/my-app (executable)
    /home/user_graal/gvme-java-hello-world/target/my-app.build_artifacts.txt (txt)
    ================================================================================================
    Finished generating 'my-app' in 2m 19s.
    ...
    ```

5. Run the native executable using:

    ```shell
    ./target/my-app
    ```

    The output should be similar to:
    ```
    Hello World!
    ```
