# Java Hello World with GraalVM Enterprise on macOS

This sample shows how you can get started quickly with GraalVM Enterprise Edition on a local machine running macOS. 

## Prerequisites

To run this sample on local (Mac OS), you need the following:

1. The latest GraalVM Enterprise 22.x for Java 17 components:
    - JDK, and
    - Native Image

2. Maven

3. Check the versions installed using:

    ```shell
    $ echo $JAVA_HOME

    /Library/Java/JavaVirtualMachines/graalvm-ee-java17-22.2.0/Contents/Home
    ```

    ```shell
    $ echo $PATH

    /Library/Java/JavaVirtualMachines/graalvm-ee-java17-22.2.0/Contents/Home/bin:/opt/apache-maven-3.8.6/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin
   ```

    ```shell
    $ java -version
    
    java version "17.0.4" 2022-07-19 LTS
    Java(TM) SE Runtime Environment GraalVM EE 22.2.0 (build 17.0.4+11-LTS-jvmci-22.2-b05)
    Java HotSpot(TM) 64-Bit Server VM GraalVM EE 22.2.0 (build 17.0.4+11-LTS-jvmci-22.2-b05, mixed mode, sharing)

    ```

    ```shell
    $ native-image --version
    
    GraalVM 22.2.0 Java 17 EE (Java Version 17.0.4+11-LTS-jvmci-22.2-b05)
    ```

    ```shell
    $ mvn --version

    Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
    Maven home: /opt/apache-maven-3.8.6
    Java version: 17.0.4, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/graalvm-ee-java17-22.2.0/Contents/Home
    Default locale: en_GB, platform encoding: UTF-8
    OS name: "mac os x", version: "12.4", arch: "x86_64", family: "mac"

    ```

## Steps
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

4. Run the GraalVM Native Image build to produce a native executable.

    ```shell
    mvn clean -Pnative -DskipTests package
    ```

    4.1) **Option 1:** With **Quick Build disabled** in the pom.xml, the output should be similar to:

    ```
    ...
    ================================================================================================
    GraalVM Native Image: Generating 'my-app' (executable)...
    ================================================================================================
    [1/7] Initializing...                                                            (6.5s @ 0.22GB)
    Version info: 'GraalVM 22.2.0 Java 17 EE'
    Java version info: '17.0.4+11-LTS-jvmci-22.2-b05'
    C compiler: cc (apple, x86_64, 13.1.6)
    Garbage collector: Serial GC
    [2/7] Performing analysis...  [*****]                                            (6.0s @ 0.76GB)
    1,803 (61.98%) of  2,909 classes reachable
    1,611 (46.25%) of  3,483 fields reachable
    7,361 (36.15%) of 20,363 methods reachable
        25 classes,     0 fields, and   260 methods registered for reflection
        49 classes,    33 fields, and    48 methods registered for JNI access
        4 native libraries: -framework Foundation, dl, pthread, z
    [3/7] Building universe...                                                       (1.1s @ 1.03GB)
    [4/7] Parsing methods...      [*]                                                (0.9s @ 0.46GB)
    [5/7] Inlining methods...     [***]                                              (0.5s @ 0.71GB)
    [6/7] Compiling methods...    [****]                                            (14.8s @ 2.14GB)
    [7/7] Creating image...                                                          (1.6s @ 2.40GB)
    2.38MB (48.42%) for code area:     3,613 compilation units
    2.41MB (49.11%) for image heap:   42,297 objects and 0 resources
    124.42KB ( 2.48%) for other data
    4.91MB in total
    ------------------------------------------------------------------------------------------------
    Top 10 packages in code area:                               Top 10 object types in image heap:
    299.36KB java.lang                                          482.68KB byte[] for code metadata
    193.80KB com.oracle.svm.jni                                 345.23KB byte[] for java.lang.String
    189.77KB java.util                                          279.49KB java.lang.String
    148.18KB com.oracle.svm.core.code                           264.13KB java.lang.Class
    119.35KB com.oracle.svm.core.genscavenge                    224.17KB byte[] for general heap data
    113.30KB java.util.concurrent                               111.71KB char[]
    75.84KB java.lang.invoke                                   102.91KB java.util.HashMap$Node
    73.33KB java.math                                           77.39KB java.util.HashMap$Node[]
    67.16KB com.oracle.svm.core                   70.43KB com.oracle.svm.core.hub.DynamicHubCompanion
    64.96KB com.oracle.svm.jni.functions                       67.90KB byte[] for reflection metadata
    1.04MB for 92 more packages                               419.44KB for 470 more object types
    ------------------------------------------------------------------------------------------------
                0.7s (2.2% of total time) in 17 GCs | Peak RSS: 3.07GB | CPU load: 2.67
    ------------------------------------------------------------------------------------------------
    Produced artifacts:
    /Users/user1/gvme-java-hello-world/target/my-app (executable)
    /Users/user1/gvme-java-hello-world/target/my-app.build_artifacts.txt (txt)
    ================================================================================================
    Finished generating 'my-app' in 32.3s.
    ...
    ```

    4.2) **Option 2:** With **Quick Build enabled** in the pom.xml, the output should be similar to:

    ```
    ...
    You enabled -Ob for this image build. This will configure some optimizations to reduce image build time.
    This feature should only be used during development and never for deployment.
    ================================================================================================
    GraalVM Native Image: Generating 'my-app' (executable)...
    ================================================================================================
    [1/7] Initializing...                                                            (7.3s @ 0.20GB)
    Version info: 'GraalVM 22.2.0 Java 17 EE'
    Java version info: '17.0.4+11-LTS-jvmci-22.2-b05'
    C compiler: cc (apple, x86_64, 13.1.6)
    Garbage collector: Serial GC
    [2/7] Performing analysis...  [*****]                                            (6.1s @ 0.46GB)
    1,764 (62.78%) of  2,810 classes reachable
    1,575 (45.88%) of  3,433 fields reachable
    7,366 (37.10%) of 19,852 methods reachable
        25 classes,     0 fields, and   260 methods registered for reflection
        49 classes,    33 fields, and    48 methods registered for JNI access
        4 native libraries: -framework Foundation, dl, pthread, z
    [3/7] Building universe...                                                       (0.8s @ 0.71GB)
    [4/7] Parsing methods...      [*]                                                (0.8s @ 0.30GB)
    [5/7] Inlining methods...     [***]                                              (0.6s @ 0.54GB)
    [6/7] Compiling methods...    [**]                                               (3.2s @ 0.89GB)
    [7/7] Creating image...                                                          (1.7s @ 1.11GB)
    1.71MB (33.67%) for code area:     4,279 compilation units
    3.22MB (63.51%) for image heap:   41,981 objects and 0 resources
    146.19KB ( 2.82%) for other data
    5.07MB in total
    ------------------------------------------------------------------------------------------------
    Top 10 packages in code area:                              Top 10 object types in image heap:
    208.78KB com.oracle.svm.jni                                359.39KB byte[] for code metadata
    183.00KB java.lang                                         342.56KB byte[] for java.lang.String
    161.87KB com.oracle.svm.core.code                          277.50KB java.lang.String
    143.89KB java.util                                         259.04KB java.lang.Class
    104.38KB com.oracle.svm.core.genscavenge                   222.54KB byte[] for general heap data
    81.11KB java.util.concurrent                               111.71KB char[]
    64.87KB java.lang.invoke                                   102.91KB java.util.HashMap$Node
    52.95KB java.math                                          77.39KB java.util.HashMap$Node[]
    44.58KB com.oracle.svm.jni.functions          68.91KB com.oracle.svm.core.hub.DynamicHubCompanion
    38.63KB com.oracle.svm.core.genscavenge.remset             66.83KB byte[] for reflection metadata
    633.63KB for 95 more packages                              418.09KB for 469 more object types
    ------------------------------------------------------------------------------------------------
                0.5s (2.2% of total time) in 16 GCs | Peak RSS: 1.48GB | CPU load: 2.26
    ------------------------------------------------------------------------------------------------
    Produced artifacts:
    /Users/user1/gvme-java-hello-world/target/my-app (executable)
    /Users/user1/gvme-java-hello-world/target/my-app.build_artifacts.txt (txt)
    ================================================================================================
    Finished generating 'my-app' in 21.5s.
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
