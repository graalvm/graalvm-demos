## Prerequisites on local macOS

To run this sample on local (Mac OS), you need the following:

1. The latest GraalVM Enterprise 22.x for Java 17 components:
    - Native Image, and
    - JDK

2. (Optional) Maven. If you don't have Maven installed, you can use the Maven wrapper (`./mvnw`) included in the Micronaut code sample.

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

    ```shell
    $ ./mvnw --version

    Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
    Maven home: /Users/graal_user/.m2/wrapper/dists/apache-maven-3.6.3-bin/1iopthnavndlasol9gbrbg6bf2/apache-maven-3.6.3
    Java version: 17.0.4, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/graalvm-ee-java17-22.2.0/Contents/Home
    Default locale: en_GB, platform encoding: UTF-8
    OS name: "mac os x", version: "12.4", arch: "x86_64", family: "mac"
    ```

Continue to **[Step 4: Setup Project and Run](./README-CS.md#step-4-setup-project-and-run)**