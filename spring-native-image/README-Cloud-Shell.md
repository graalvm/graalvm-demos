# Spring Boot Microservice with GraalVM Enterprise in OCI Cloud Shell

This example shows how you can get started quickly with GraalVM Enterprise Edition in Oracle Cloud Infrastructre (OCI) Cloud Shell. This example uses a Spring Boot application built with GraalVM Enterprise Native Image and JDK (Java Development Kit).

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


## Step 4: Set up your project, build and run a JAR

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
    
    This will create a binary executable `target/jibber`.

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
