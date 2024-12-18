## [OPTIONAL] Confirm Software Version and Environment Variables

1. Confirm GraalVM is the current JDK:

    ```shell
    csruntimectl java list
    ```

    The output should be similar to (versions may vary):

    ```shell
    * graalvmjdk-17                                                      /usr/lib64/graalvm/graalvm-java17
      oraclejdk-11                                                                   /usr/java/jdk-11.0.17
      oraclejdk-1.8                                                        /usr/lib/jvm/jdk-1.8-oracle-x64
    ```

2. Confirm the environment variable `JAVA_HOME` is set correctly:

    ```shell
    echo $JAVA_HOME
    ```

    The output should be similar to:

    ```shell
    /usr/lib64/graalvm/graalvm-java17
    ```

3. Confirm the environment variable `PATH` is set correctly:

    ```shell
    echo $PATH
    ```

    The output should be similar to:

    ```shell
    /usr/lib64/graalvm/graalvm-java17/bin/:...
    ```

4. Confirm the `java` version:

    ```shell
    java -version
    ```

    The output should be similar to (versions may vary):

    ```shell
    java version "17.0.13" 2024-10-15 LTS
    Java(TM) SE Runtime Environment Oracle GraalVM 17.0.13+10.1 (build 17.0.13+10-LTS-jvmci-23.0-b49)
    Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 17.0.13+10.1 (build 17.0.13+10-LTS-jvmci-23.0-b49, mixed mode, sharing)
    ```

5. Confirm the `native-image` version:

    ```shell
    native-image --version
    ```

    The output should be similar to (versions may vary):

    ```shell
    native-image 17.0.13 2024-10-15
    GraalVM Runtime Environment Oracle GraalVM 17.0.13+10.1 (build 17.0.13+10-LTS-jvmci-23.0-b49)
    Substrate VM Oracle GraalVM 17.0.13+10.1 (build 17.0.13+10-LTS, serial gc, compressed references)
    ```

6. Confirm the `Java` used for Maven builds:

    ```shell
    ./mvnw --version
    ```

    The output should be similar to (versions may vary):

    ```shell
    ...
    Java version: 17.0.13, vendor: Oracle Corporation, runtime: /usr/lib64/graalvm/graalvm-java17
    ...
    ```

Go back to continue with the rest of the example.
