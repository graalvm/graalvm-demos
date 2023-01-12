# Spring Boot Native Image Microservice Example

This demo shows how to build, package, and run a simple Spring Boot 3 microservice from a JAR file with the GraalVM JDK, and from a native executable with GraalVM Native Image. 
The benefits of using a native executable are much smaller size, faster start-up times, and reduced memory consumption.
It also demonstrates how to run the application and build the native executable within a Docker container. 

There are two ways to generate a native executable from a Spring Boot application:

- [Using Buildpacks](https://docs.spring.io/spring-boot/docs/3.0.0/reference/html/native-image.html#native-image.developing-your-first-application.buildpacks)
- [Using GraalVM Native Build Tools](https://docs.spring.io/spring-boot/docs/3.0.0/reference/html/native-image.html#native-image.developing-your-first-application.native-build-tools)

### Note on a Sample Application

The example is a minimal REST-based API application, built on top of Spring Boot 3. It consists of:

- `com.example.jibber.JibberApplication`: the main Spring Boot class.
- `com.example.jibber.Jabberwocky`: a utility class that implements the logic of the application.
- `com.example.jibber.JibberController`: a REST controller which serves as an entry-point for HTTP requests.

If you call the HTTP endpoint, `/jibber`, it will return some nonsense verse generated in the style of the Jabberwocky poem, by Lewis Carroll. 
The program achieves this by using a Markov Chain to model the original poem (this is essentially a statistical model). 
This model generates a new text.
The example application provides the text of the poem, then generates a model of the text, which the application then uses to generate a new text that is similar to the original text. 
The application uses the [RiTa library](https://rednoise.org/rita/) as an external dependency to build and use Markov Chains.

By default, the demo uses the [Native Build Tools Maven plugin](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html) to perform the tasks.
If you would like to run this demo using [BuildPacks](https://docs.spring.io/spring-boot/docs/3.0.0/reference/html/native-image.html#native-image.developing-your-first-application.buildpacks), the build configuration is provided for you too.

## Preparation

1. Download and  install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) 
    ```
2. (Optional) Install and run Docker. See [Get Docker](https://docs.docker.com/get-docker/#installation) for more details. Configure it to [allow non-root user](https://docs.docker.com/engine/install/linux-postinstall/#manage-docker-as-a-non-root-user) if you are on Linux.

3. Download the demos repository or clone it as follows:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
4. Change directory to the demo subdirectory:
    ```bash
    cd spring-native-image
    ```
## Build and Run as a JAR

This project is built using Maven. 

1. Build the application on top of a JVM:
    ```shell
    mvn clean package
    ```
    It generates a runnable JAR file that contains all of the application’s dependencies and also a correctly configured `MANIFEST` file.

2. Test running this application from a JAR:
    ```shell
    java -jar ./target/benchmark-jibber-0.0.1-SNAPSHOT.jar &
    ```
    where `&` brings the application to the background. 
    
3. Open the application [http://localhost:8080/jibber](http://localhost:8080/jibber) in a browser, or call the endpoint using `curl`:
    ```shell
    curl http://localhost:8080/jibber
    ```
    It should generate a random nonsense verse in the style of the poem Jabberwocky by Lewis Carrol. 
    To terminate it, first bring the application to the foreground using `fg`, and then enter `<CTRL-c>`.

## Run in a Docker Container

As a nice extra, there is a Dockerfile provided with this demo. So, besides building the application JAR, you see a Docker image built at the `mvn clean package` step, pulling the GraalVM container image, `ghcr.io/graalvm/jdk:ol8-java17`, as the JVM.

Run the Docker image in a container:
```shell
docker run --rm --name graalce -d -p 8080:8080 jibber-benchmark:graalce.0.0.1-SNAPSHOT
```

You can then test the container suing `curl` exactly as you did before - remember to allow a little time for the application to start up.

## Build and Run as a Native Executable

With the built-in support for GraalVM Native Image in Spring Boot 3, superseding the experimental [Spring Native project](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#overview), it has become much easier to compile a Spring Boot 3 application into a native executable.

1. Run the following command:

    ```shell
    mvn native:compile -Pnative
    ```
    The `-Pnative` profile is used to turn on building a native executable.
    It will generate a native executable for your platform in the _target_ directory, called _benchmark-jibber_.

    To build using BuildPacks, run the `mvn spring-boot:build-image -Pnative` command to generate a native executable. For more information about using BuildPacks to create a native executable, see [Building a Native Image Using Buildpacks](https://docs.spring.io/spring-boot/docs/3.0.0/reference/html/native-image.html#native-image.developing-your-first-application.buildpacks).

2. Run this native executable and put it into the background, by appending `&`:
    ```shell
    ./target/benchmark-jibber &
    ```

3. Open the application [http://localhost:8080/jibber](http://localhost:8080/jibber) in a browser, or call the endpoint using `curl`:

    ```shell
    curl http://localhost:8080/jibber
    ```
    You should get some nonsense verse back. 
    Terminate it, first bring the application to the foreground using `fg`, and then enter `<CTRL-c>`.

From the log output, notice how much quicker the native executable version of this Spring Boot application starts. It also uses fewer resources than running from a JAR file.
### Configure Native Build Tools Maven Plugin

You can configure the Maven plugin for GraalVM Native Image using the `<buildArgs>` elements. 
In individual `<buildArg>` elements, you can pass all Native Image options as you would pass them to the `native-image` tool on the command line. 
For example, pass the `-Ob` (capital “O”, lower case “b”) option which enables the quick build mode for development purposes. 
Also change the resulting binary name to "new-jibber".

1. Open _pom.xml_ and modify the `native-maven-plugin` configuration as follows:

    ```xml
    <plugin>
        <groupId>org.graalvm.buildtools</groupId>
        <artifactId>native-maven-plugin</artifactId>
        <configuration>
            <imageName>new-jibber</imageName>
            <buildArgs>
                <buildArg>-Ob</buildArg>
            </buildArgs>
        </configuration>
    </plugin>
    ```

2. Now re-build the native executable using the `native` profile:

    ```shell
    ./mvnw native:compile -Pnative
    ```
    
    Notice that a native executable, now named `new-jibber`, was generated in less time: the compiler operated in economy mode with fewer optimizations, resulting in much faster compilation times. (The quick build mode is not recommended for production.)

See the [Native Build Tools Maven plugin documentation](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html) to learn more. 
## Containerize the Native Executable

If you are using macOS or Windows, to build a Docker image containing your native executable you need to build the native executable within a Docker container. How to do this is described below. 

If you are a Linux user, you can easily containerise the native executable using the following command:
```shell
docker build -f Dockerfiles/Dockerfile.native --build-arg APP_FILE=benchmark-jibber -t jibber-benchmark:native.0.0.1-SNAPSHOT .
```

Once that is built, you can test it as follows:
```shell
docker run --rm --name native -d -p 8080:8080 jibber-benchmark:native.0.0.1-SNAPSHOT
```
### Build a Native Image Container on Something Other than Linux

If you are not using Linux as your operating system, you need to build the native executable within a Docker container. To do this we provided a two-stage Docker build file. 

1. Run this command to build the native executable within a Docker container:
    ```shell
    docker build -f Dockerfiles/Dockerfile -t jibber-benchmark:native.0.0.1-SNAPSHOT .
    ```

2. Once that is built, you can test it as follows:
    ```shell
    docker run --rm --name native -d -p 8080:8080 jibber-benchmark:native.0.0.1-SNAPSHOT
    ```
## Measure the Performance of the Application and Metrics

The Spring Actuator dependency has been added to the project, along with support for Prometheus. 
If you want to test the performance of either the JVM version, or the native executable version of the application, you can make use of the Prometheus support. 
If you are hosting the application locally, it is available on port 8080:

[http://localhost:8080/actuator/prometheus](http://localhost:8080/actuator/prometheus)

## Related Documentation

- Run an interactive lab: [GraalVM Native Image, Spring and Containerisation](https://luna.oracle.com/lab/fdfd090d-e52c-4481-a8de-dccecdca7d68)
- [OCI Cloud Shell](https://docs.oracle.com/en/graalvm/enterprise/22/docs/getting-started/oci/cloud-shell/)
- [Native Build Tools](https://graalvm.github.io/native-build-tools/)
- [Spring Boot GraalVM Native Image Support](https://docs.spring.io/spring-boot/docs/3.0.0/reference/html/native-image.html#native-image.developing-your-first-application.native-build-tools)
