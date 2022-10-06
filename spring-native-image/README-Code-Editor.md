# Spring Boot Microservice with GraalVM Enterprise in OCI Code Editor

This example shows how you can get started quickly with GraalVM Enterprise Edition in Oracle Cloud Infrastructre (OCI) Code Editor. This example uses a Spring Boot application built with GraalVM Enterprise Native Image and JDK (Java Development Kit).

## What is GraalVM?

[GraalVM](https://www.oracle.com/in/java/graalvm/) is a high-performance JDK distribution that accelerates Java workloads. GraalVM Native Image ahead-of-time compilation builds your Java application into a native executable that is small, starts fast, and uses less memory and CPU. Leading Java microservices frameworks such as Spring Boot, Micronaut, Quarkus and Helidon support GraalVM Native Image.

GraalVM Enterprise Edition is available for use on Oracle Cloud Infrastructure (OCI) at no additional cost.

## What is Code Editor?

[Code Editor](https://www.oracle.com/devops/code-editor/) enables you to edit and deploy code directly from the Oracle Cloud Console. You can develop applications, service workflows, and scripts entirely from a browser. This makes it easy to rapidly prototype cloud solutions, try new services, and accomplish quick coding tasks.

GraalVM Enterprise JDK 17 and Native Image are preinstalled in Cloud Shell, so you don’t have to install and configure a development machine to get started. Code Editor's integration with Cloud Shell gives you direct access to GraalVM Enterprise JDK 17 and Native Image.

## Step 1: Open Terminal in Code Editor 

1. [Login to OCI Console and launch Code Editor](https://cloud.oracle.com/?bdcstate=maximized&codeeditor=true).

2. Open a `New Terminal` in Code Editor. Use this Terminal window to run the commands shown in this sample.
![](./images/oci-ce-terminal.png)


## Step 2: Select GraalVM as the current JDK 

1. List the installed JDKs:

    ```shell
    csruntimectl java list
    ```

    The output should be similar to:

    ```shell
    * graalvmeejdk-17.0.4.1                                         /usr/lib64/graalvm/graalvm22-ee-java17
      openjdk-11.0.16.1                        /usr/lib/jvm/java-11-openjdk-11.0.16.1.1-1.0.1.el7_9.x86_64
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
    git init graalvmee-spring-native-image

    cd graalvmee-spring-native-image

    git remote add origin https://github.com/graalvm/graalvm-demos.git

    git config core.sparsecheckout true

    echo "spring-native-image/*">>.git/info/sparse-checkout

    git pull --depth=1 origin master

    cd spring-native-image

    ```

2. Build a JAR file for the example app.

    ```shell
    mvn clean package
    ```

3. Run the app JAR in the background.

    ```shell
    java -jar ./target/benchmark-jibber-0.0.1-SNAPSHOT.jar &
    ```

4. Test the app JAR. 

    ```shell
    curl http://localhost:8080/jibber
    ```

    It should generate a random nonsense verse in the style of the poem Jabberwocky by Lewis Carrol. The output should be similar to:

    ```shell
    ...
    And, as in uffish thought he stood, The Jabberwock, my beamish boy!<br/>
    ’Twas brillig, and the slithy toves Did gyre and gimble in the wabe: All mimsy were the borogoves, And burbled as it came!<br/>
    He left it dead, and the slithy toves Did gyre and gimble in the wabe: All mimsy were the borogoves, And the mome raths outgrabe.<br/>
    Beware the Jubjub bird, and the slithy toves Did gyre and gimble in the wabe: All mimsy were the borogoves, And burbled as it came!<br/>
    Beware the Jabberwock, my beamish boy!<br/>
    Beware the Jubjub bird, and the slithy toves Did gyre and gimble in the wabe: All mimsy were the borogoves, And the mome raths outgrabe.<br/>
    ’Twas brillig, and with its head He went galumphing back.<br/>
    And, as in uffish thought he stood, The Jabberwock, my son!<br/>
    Come to my arms, my son!<br/>
    ’Twas brillig, and shun The frumious Bandersnatch!<br/>
    ```

5. Bring the running app JAR in the foreground

    ```shell
    fg
    ```

6. Once the app is running in the foreground, press CTRL+C to stop it.


## Step 5: Build and run a native executable

Let's build a native executable for our Spring Boot microservice using GraalVM Enterprise Native Image. In this example, we'll use the Maven plugin from [GraalVM Native Build Tools](https://graalvm.github.io/native-build-tools/latest/index.html) and
[Spring Native](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/).

1. Build the app native executable

    ```shell
    export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false

    mvn package -Dnative

    ```
    
    This will create a binary executable `target/jibber`. The output should be similar to:

    ```shell
    ...
    You enabled -Ob for this image build. This will configure some optimizations to reduce image build time.
    ...
    ==============================================================================================================
    GraalVM Native Image: Generating 'jibber' (executable)...
    ==============================================================================================================
    Warning: Could not register org.springframework.boot.actuate.health.ReactiveHealthEndpointWebExtension: allPublicMethods for reflection. Reason: java.lang.NoClassDefFoundError: reactor/core/publisher/Mono.
    Warning: Could not register org.springframework.boot.autoconfigure.jdbc.HikariDriverConfigurationFailureAnalyzer: allDeclaredConstructors for reflection. Reason: java.lang.NoClassDefFoundError: org/springframework/jdbc/CannotGetJdbcConnectionException.
    Warning: Could not register org.springframework.boot.diagnostics.analyzer.ValidationExceptionFailureAnalyzer: allDeclaredConstructors for reflection. Reason: java.lang.NoClassDefFoundError: javax/validation/ValidationException.
    Warning: Could not register org.springframework.boot.liquibase.LiquibaseChangelogMissingFailureAnalyzer: allDeclaredConstructors for reflection. Reason: java.lang.NoClassDefFoundError: liquibase/exception/ChangeLogParseException.
    [1/7] Initializing...                                                                         (16.9s @ 0.29GB)
    Version info: 'GraalVM 22.2.0.1 Java 17 EE'
    Java version info: '17.0.4.1+1-LTS-jvmci-22.2-b08'
    C compiler: gcc (redhat, x86_64, 11.2.1)
    Garbage collector: Serial GC
    1 user-specific feature(s)
    - com.oracle.svm.thirdparty.gson.GsonFeature
    The bundle named: org.apache.tomcat.util.threads.res.LocalStrings, has not been found. If the bundle is part of a module, verify the bundle name is a fully qualified class name. Otherwise verify the bundle path is accessible in the classpath.
    Warning: Could not register complete reflection metadata for org.springframework.validation.beanvalidation.SpringValidatorAdapter$ViolationFieldError. Reason(s): java.lang.NoClassDefFoundError: javax/validation/Validator
    [2/7] Performing analysis...  [*******]                                                      (156.8s @ 3.03GB)
    15,660 (91.30%) of 17,153 classes reachable
    23,895 (66.64%) of 35,857 fields reachable
    81,149 (65.27%) of 124,321 methods reachable
        919 classes,   268 fields, and 4,426 methods registered for reflection
        63 classes,    68 fields, and    55 methods registered for JNI access
        4 native libraries: dl, pthread, rt, z
    [3/7] Building universe...                                                                    (26.2s @ 1.15GB)
    [4/7] Parsing methods...      [*****]                                                         (23.5s @ 2.01GB)
    [5/7] Inlining methods...     [***]                                                           (12.4s @ 2.12GB)
    [6/7] Compiling methods...    [********]                                                      (71.8s @ 2.37GB)
    [7/7] Creating image...                                                                       (12.6s @ 2.49GB)
    24.86MB (47.56%) for code area:    56,213 compilation units
    27.10MB (51.84%) for image heap:  378,545 objects and 366 resources
    319.38KB ( 0.60%) for other data
    52.27MB in total
    --------------------------------------------------------------------------------------------------------------
    Top 10 packages in code area:                          Top 10 object types in image heap:
    1.98MB com.oracle.svm.core.code                        5.69MB byte[] for code metadata
    1.18MB sun.security.ssl                                3.47MB byte[] for java.lang.String
    824.27KB java.util                                       3.35MB byte[] for embedded resources
    545.81KB org.apache.catalina.core                        2.72MB java.lang.Class
    499.69KB org.apache.tomcat.util.net                      2.66MB java.lang.String
    490.02KB org.apache.coyote.http2                         2.36MB byte[] for general heap data
    465.23KB com.sun.crypto.provider                       944.23KB byte[] for reflection metadata
    438.78KB java.lang.invoke                              734.06KB com.oracle.svm.core.hub.DynamicHubCompanion
    417.30KB java.text                                     445.91KB c.o.s.core.hub.DynamicHub$ReflectionMetadata
    361.50KB sun.nio.ch                                    409.16KB java.util.concurrent.ConcurrentHashMap$Node
    17.36MB for 666 more packages                           3.62MB for 3246 more object types
    --------------------------------------------------------------------------------------------------------------
                    19.1s (5.8% of total time) in 42 GCs | Peak RSS: 4.36GB | CPU load: 1.74
    --------------------------------------------------------------------------------------------------------------
    Produced artifacts:
    /home/graal_user/spring-native-image/target/jibber (executable)
    /home/graal_user/spring-native-image/target/jibber.build_artifacts.txt (txt)
    ==============================================================================================================
    Finished generating 'jibber' in 5m 30s.
    ...
    ```

2. Run the app native executable in the background

    ```shell
    ./target/jibber &
    ```

3. Test the app native executable

    ```shell
    curl http://localhost:8080/jibber
    ```

    It should generate a random nonsense verse in the style of the poem Jabberwocky by Lewis Carrol. The output should be similar to:

    ```shell
    ...
    The Jabberwock, my beamish boy!<br/>
    Beware the Jubjub bird, and the slithy toves Did gyre and gimble in the wabe: All mimsy were the borogoves, And the mome raths outgrabe.<br/>
    Beware the Jubjub bird, and the slithy toves Did gyre and gimble in the wabe: All mimsy were the borogoves, And burbled as it came!<br/>
    Beware the Jabberwock, with eyes of flame, Came whiffling through the tulgey wood, And the mome raths outgrabe.<br/>
    He left it dead, and shun The frumious Bandersnatch!<br/>
    He left it dead, and the slithy toves Did gyre and gimble in the wabe: All mimsy were the borogoves, And the mome raths outgrabe.<br/>
    ’Twas brillig, and the slithy toves Did gyre and gimble in the wabe: All mimsy were the borogoves, And burbled as it came!<br/>
    ’Twas brillig, and with its head He went galumphing back.<br/>
    Come to my arms, my son!<br/>
    Beware the Jabberwock, with eyes of flame, Came whiffling through the tulgey wood, And burbled as it came!<br/>
    ```

4. Bring the running app native executable in the foreground

    ```shell
    fg
    ```

5. Once the app is running in the foreground, press CTRL+C to stop it.
