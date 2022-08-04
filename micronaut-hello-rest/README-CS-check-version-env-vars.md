## Step 3: [OPTIONAL] Confirm Software Version and Environment Variables

1. Confirm GraalVM is the current JDK:

    ```shell
    csruntimectl java list
    ```

    The output should be similar to:

    ```shell
    * graalvmeejdk-17.0.4                                    /usr/lib64/graalvm/graalvm22-ee-java17
      openjdk-11.0.15                   /usr/lib/jvm/java-11-openjdk-11.0.15.0.9-2.0.1.el7_9.x86_64
      openjdk-1.8.0.332                /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.332.b09-1.el7_9.x86_64
    ```

2. Confirm the environment variable `JAVA_HOME` is set correctly:

    ```shell
    echo $JAVA_HOME
    ```

    The output should be similar to:

    ```shell
    /usr/lib64/graalvm/graalvm22-ee-java17
    ```

3. Confirm the environment variable `PATH` is set correctly:

    ```shell
    echo $PATH
    ```

    The output should be similar to:

    ```shell
    /usr/lib64/graalvm/graalvm22-ee-java17/bin/:/ggs_client/usr/bin: ...
    ```

4. Confirm the `java` version:

    ```shell
    java -version
    ```

    The output should be similar to:

    ```shell
    java version "17.0.4" 2022-07-19 LTS   
    Java(TM) SE Runtime Environment GraalVM EE 22.2.0 (build 17.0.4+11-LTS-jvmci-22.2-b05)   
    Java HotSpot(TM) 64-Bit Server VM GraalVM EE 22.2.0 (build 17.0.4+11-LTS-jvmci-22.2-b05, mixed mode, sharing)
    ```

5. Confirm the `native-image` version:

    ```shell
    native-image --version
    ```

    The output should be similar to:

    ```shell
    GraalVM 22.2.0 Java 17 EE (Java Version 17.0.4+11-LTS-jvmci-22.2-b05)
    ```

6. Confirm the `maven` version and `Java` used:

    ```shell
    mvn --version
    ```

    The output should be similar to:

    ```shell
    Apache Maven 3.6.1 (Red Hat 3.6.1-6.3)
    Maven home: /opt/rh/rh-maven36/root/usr/share/maven
    Java version: 17.0.4, vendor: Oracle Corporation, runtime: /usr/lib64/graalvm/graalvm22-ee-java17   
    Default locale: en_US, platform encoding: UTF-8
    OS name: "linux", version: "4.14.35-2047.513.2.2.el7uek.x86_64", arch: "amd64", family: "unix"
    ```

Continue to **[Step 4: Setup Project and Run](./README-CS.md#step-4-setup-project-and-run)**