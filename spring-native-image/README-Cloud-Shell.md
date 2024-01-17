# Spring Boot Microservice with Oracle GraalVM in OCI Cloud Shell

This part shows how you can get started quickly with Oracle GraalVM in Oracle Cloud Infrastructure (OCI) Cloud Shell using the Spring Boot 3 microservice example.

Oracle GraalVM is available for use on Oracle Cloud Infrastructure (OCI) at no additional cost.

## What is Cloud Shell?

[Cloud Shell](https://www.oracle.com/devops/cloud-shell/) is a free-to-use browser-based terminal accessible from the Oracle Cloud Console. It provides access to a Linux shell with preinstalled developer tools and a pre-authenticated OCI CLI. You can use the shell to interact with OCI resources, follow labs and tutorials, and quickly run utility commands.

Oracle GraalVM for JDK 17 (with Native Image) is preinstalled in Cloud Shell, so you don’t have to install and configure a development machine to get started.

## Step 1: Launch Cloud Shell

[Login to OCI Console and launch Cloud Shell](https://cloud.oracle.com/?bdcstate=maximized&cloudshell=true).

## Step 2: Select GraalVM as the Current JDK

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

## Step 3: (Optional) Confirm Software Version and Environment Variables

This step is optional - [Check software version and environment variables](../_common/README-check-version-env-vars.md)

## Step 4: Set up Your Project, Build and Run a JAR

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

## Step 5: Build and Run a Native Executable

Now build a native executable for your Spring Boot microservice using Oracle GraalVM Native Image.

1. Build the app native executable

    ```shell
    mvn -Pnative native:compile
    ```

    This will create a binary executable `target/benchmark-jibber`.

2. Run the app native executable in the background

    ```shell
    ./target/benchmark-jibber &
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