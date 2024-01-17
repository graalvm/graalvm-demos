# Spring Boot Native Image Microservice

This demo shows how to build, package, and run a simple Spring Boot 3 microservice from a JAR file with the GraalVM JDK, and from a native executable with GraalVM Native Image. The benefits of using a native executable are faster start-up times and reduced memory consumption. It also demonstrates how to run the application and build the native executable within a Docker container.

There are two ways to generate a native executable from a Spring Boot application:
- [Using GraalVM Native Build Tools](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.developing-your-first-application.native-build-tools)
- [Using Buildpacks](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.developing-your-first-application.buildpacks)

## Sample Application

The example is a minimal REST-based API application, built on top of Spring Boot 3. It consists of:

- `com.example.jibber.JibberApplication`: the main Spring Boot class. It is also a REST controller which serves as an entry-point for HTTP requests.
- `com.example.jibber.Jabberwocky`: a utility class that implements the logic of the application.

If you call the HTTP endpoint, `/jibber`, it will return some nonsense verse generated in the style of the Jabberwocky poem, by Lewis Carroll. The program achieves this by using a Markov Chain to model the original poem (this is essentially a statistical model). This model generates a new text. The example application provides the text of the poem, then generates a model of the text, which the application then uses to generate a new text that is similar to the original text. The application uses the [RiTa library](https://rednoise.org/rita/) as an external dependency to build and use Markov Chains.

By default, the demo uses the [Native Build Tools Maven plugin](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html) to perform the tasks. If you would like to run this demo using [BuildPacks](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.developing-your-first-application.buildpacks), the build configuration is provided for you too.

## Prerequisites

1. Download and install the latest GraalVM JDK using [SDKMAN!](https://sdkman.io/).
    ```bash
    sdk install java 21.0.2-graal
    ```

2. (Optional) Install and run a Docker-API compatible container runtime such as [Rancher Desktop](https://docs.rancherdesktop.io/getting-started/installation/), [Docker](https://www.docker.io/gettingstarted/), or [Podman](https://podman.io/docs/installation). If you are using Docker, configure it to [allow non-root user access](https://docs.docker.com/engine/install/linux-postinstall/#manage-docker-as-a-non-root-user) if you are on Linux.

3. Download the demos repository or clone it as follows:

    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```

4. Change directory to the demo subdirectory:

    ```bash
    cd spring-native-image
    ```

## Application JAR

### Build and Run as a JAR

This demo is built using Maven.

1. Build the application on top of a JVM:

    ```shell
    ./mvnw clean package
    ```

    It generates a runnable JAR file that contains all of the application’s dependencies and also a correctly configured `MANIFEST` file.

2. Run the application JAR and put it into the background by appending `&`:

    ```shell
    java -jar ./target/benchmark-jibber-0.0.1-SNAPSHOT.jar &
    ```

3. Open the application [http://localhost:8080/jibber](http://localhost:8080/jibber) in a browser, or call the endpoint using `curl`:

    ```shell
    curl http://localhost:8080/jibber
    ```

    It should generate a random nonsense verse in the style of the poem Jabberwocky by Lewis Carrol. 

4. Bring the application to the foreground using `fg`, and then enter `<CTRL-c>` to terminate the application.

### (Optional) Containerize the JAR

The following steps (5-8) show how you can easily containerize the JAR built in the previous step using the Oracle GraalVM JDK container image `container-registry.oracle.com/graalvm/jdk:17-ol8`.

5. Run this command to package the JAR as a Docker container:

    ```shell
    docker build -f Dockerfiles/Dockerfile.jvm --build-arg APP_FILE=benchmark-jibber-0.0.1-SNAPSHOT.jar -t jibber-benchmark:jvm.0.0.1-SNAPSHOT .
    ```

6. Run the container:

    ```shell
    docker run --rm --name graal -p 8080:8080 jibber-benchmark:jvm.0.0.1-SNAPSHOT
    ```

7. Open the application [http://localhost:8080/jibber](http://localhost:8080/jibber) in a browser, or from a new terminal window, call the endpoint using `curl`:

    ```shell
    curl http://localhost:8080/jibber
    ```

    You should get a random nonsense verse in the style of the poem Jabberwocky by Lewis Carrol. 

8. To stop the application, first get the container id using `docker ps`, and then run:

    ```shell
    docker rm -f <container_id>
    ```

## Native Executable

Recap what you have so far: built a Spring Boot application with an HTTP endpoint, and successfully containerised it.
Now you will look at how you can create a native executable from your application.

Spring Boot 3's built-in support for GraalVM Native Image makes it easy to compile a Spring Boot 3 application into a native executable.

This native executable not only starts faster but also uses far fewer resources than running the application as a JAR file.

You can use the `native-image` tool from the GraalVM installation to build a native executable.
In this example, you'll use the [GraalVM Native Build Tools for Maven](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html) to build a native executable.

### Default Native Build Configuration

Make sure you’re using `spring-boot-starter-parent` in order to inherit the out-of-the-box `native` profile, and the `org.graalvm.buildtools:native-maven-plugin` plugin.

You should see the following in the Maven `pom.xml` file:

``` xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

``` xml
<build>
    <plugins>
        <plugin>
            <groupId>org.graalvm.buildtools</groupId>
            <artifactId>native-maven-plugin</artifactId>
        </plugin>
        ...
    </plugins>
</build>
```

The out-of-the-box `native` profile has [GraalVM Reachability Metadata](https://www.graalvm.org/native-image/libraries-and-frameworks/) enabled by default.

### Build and Run as a Native Executable

With the out-of-the-box `native` profile active, you can invoke the `native:compile` goal to trigger native-image compilation.

1. Run the following command:

    ```shell
    ./mvnw native:compile -Pnative
    ```

   The `native` profile is used to generate a native executable for your platform. The native executable is called _benchmark-jibber_ and is generated in the _target_ directory.

   >Alternatively, to build using BuildPacks, run the `./mvnw spring-boot:build-image -Pnative` command to generate a native executable. For more information about using BuildPacks to create a native executable, see [Building a Native Image Using Buildpacks](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.developing-your-first-application.buildpacks).

2. Run the native executable and put it into the background by appending `&`:

    ```shell
    ./target/benchmark-jibber &
    ```

3. Open the application [http://localhost:8080/jibber](http://localhost:8080/jibber) in a browser, or call the endpoint using `curl`:

    ```shell
    curl http://localhost:8080/jibber
    ```

   You should get a random nonsense verse in the style of the poem Jabberwocky by Lewis Carrol.

4. Bring the application to the foreground using `fg`, and then enter `<CTRL-c>` to terminate the application.

From the log output, notice how much quicker the native executable version of this Spring Boot application starts compared to the JAR. The native executable also uses fewer resources than running from a JAR file.

### Additional Native Build Configuration

Notice that you can pass additional configuration arguments to the underlying `native-image` build tool using the `<buildArgs>` section. In individual `buildArg` tags, you can pass parameters exactly the same way as you do from a command line. This lets you use all of the parameters that work with the `native-image` tool from Maven.

Add the following snippet to the pom.xml to pass additional arguments to enable verbose output, quick build mode, etc. over and above the out-of-the-box `native` profile.

```xml
<profiles>
   <profile>
      <id>native</id>
      <build>
         <plugins>
            <plugin>
               <groupId>org.graalvm.buildtools</groupId>
               <artifactId>native-maven-plugin</artifactId>
               <!--<version>0.9.28</version>-->
               <configuration>
                  <verbose>true</verbose>
                  <quickBuild>true</quickBuild>
                  <buildArgs combine.children="append">
                     <arg>-H:+ReportExceptionStackTraces</arg>
                  </buildArgs>
               </configuration>
            </plugin>
         </plugins>
      </build>
   </profile>
</profiles>
```

### Build and Run as a Native Executable

Let's rebuild the native executable with the additional configuration arguments.

1. Run the following command:

    ```shell
    ./mvnw native:compile -Pnative
    ```

    With the quick build mode enabled, it takes less time to build the native executable. This mode should be used in development for faster builds.

2. Run the native executable and put it into the background by appending `&`:

    ```shell
    ./target/benchmark-jibber &
    ```

3. Open the application [http://localhost:8080/jibber](http://localhost:8080/jibber) in a browser, or call the endpoint using `curl`:

    ```shell
    curl http://localhost:8080/jibber
    ```

    You should get a random nonsense verse in the style of the poem Jabberwocky by Lewis Carrol. 

4. Bring the application to the foreground using `fg`, and then enter `<CTRL-c>` to terminate the application.

From the log output, notice how much quicker the native executable version of this Spring Boot application starts compared to the JAR. The native executable also uses fewer resources than running from a JAR file.

### (Optional) Containerize the Native Executable on Linux

The following steps (5-8) are for Linux only.

5. On Linux, you can easily containerise the native executable using the following command:

    ```shell
    docker build -f Dockerfiles/Dockerfile.native --build-arg APP_FILE=benchmark-jibber -t jibber-benchmark:native.0.0.1-SNAPSHOT .
    ```

6. Run the application:

    ```shell
    docker run --rm --name native -p 8080:8080 jibber-benchmark:native.0.0.1-SNAPSHOT
    ```

7. Open the application [http://localhost:8080/jibber](http://localhost:8080/jibber) in a browser, or from a new terminal window, call the endpoint using `curl`:

    ```shell
    curl http://localhost:8080/jibber
    ```

    It should generate a random nonsense verse in the style of the poem Jabberwocky by Lewis Carrol. 

8. To stop the application, first get the container id using `docker ps`, and then run:

    ```shell
    docker rm -f <container_id>
    ```

### (Optional) Use Multistage Docker Builds to Build a Native Image and Package it in a Lightweight Container

The following steps (9-12) are for all platforms - MacOS, Windows, and Linux. 

For MacOS and Windows, to build a Docker image containing your native executable, you need to build the native executable inside a Docker container. To do this, we've provided a [multistage Docker build file](./Dockerfiles/Dockerfile). 

9. Run this command to build the native executable within a Docker container:

    ```shell
    docker build -f Dockerfiles/Dockerfile -t jibber-benchmark:native.0.0.1-SNAPSHOT .
    ```

10. Run the application:

    ```shell
    docker run --rm --name native -p 8080:8080 jibber-benchmark:native.0.0.1-SNAPSHOT
    ```

11. Open the application [http://localhost:8080/jibber](http://localhost:8080/jibber) in a browser, or from a new terminal window, call the endpoint using `curl`:

    ```shell
    curl http://localhost:8080/jibber
    ```

    It should generate a random nonsense verse in the style of the poem Jabberwocky by Lewis Carrol. 

12. To stop the application, first get the container id using `docker ps`, and then run:

    ```shell
    docker rm -f <container_id>
    ```

## Measure the Performance of the Application and Metrics

The Spring Actuator dependency has been added to the project, along with support for Prometheus. If you want to test the performance of either the JVM version, or the native executable version of the application, you can make use of the Prometheus support. If you are hosting the application locally, it is available on port 8080:

[http://localhost:8080/actuator/prometheus](http://localhost:8080/actuator/prometheus)

## Related Documentation

- Run an interactive lab: [Level Up your Spring Boot Java Application with GraalVM](https://luna.oracle.com/lab/268ea851-2f09-43e6-8d70-40a10cb4de03)
- [Maven Build Plugin for GraalVM Native Image](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html/)
- [Spring Boot GraalVM Native Image Support](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html)
