# Java Multithreading Demo For GraalVM Dashboard

Multithreading demo is provided to showcase the applicability of [GraalVM Dashboard](https://www.graalvm.org/docs/tools/dashboard/) - a web-based dashboard for visualizing aspects of dynamic and static compilations in GraalVM, in particular, [Native Image](https://www.graalvm.org/reference-manual/native-image/).

The demo is a Java program which does synchronous and asynchronous threads execution.
Each thread loops through exactly the same array of integers and generates a stream of pseudorandom numbers.
The program calculates the time taken to perform the task synchronously and asynchronously.

Multithreading demo is comprised of two sub-projects, each built with Maven: Multithreading Demo Oversized and Multithreading Demo Improved.
The _pom.xml_ file of each sub-project includes the [Native Image Maven plugin](https://www.graalvm.org/reference-manual/native-image/NativeImageMavenPlugin/), which instructs Maven to generate a native image of a JAR file with all dependencies at the `mvn package` step.
The plugin will also gather the diagnostic information during the native image build and write that data to a dump file in the target directory.

```xml
<plugin>
    <groupId>org.graalvm.nativeimage</groupId>
    <artifactId>native-image-maven-plugin</artifactId>
    <version>${graalvm.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>native-image</goal>
            </goals>
            <phase>package</phase>
        </execution>
    </executions>
    <configuration>
        <skip>false</skip>
        <imageName>multithreading-image</imageName>
        <buildArgs>
            --no-fallback -H:DashboardDump=dumpfile -H:+DashboardAll --initialize-at-build-time
        </buildArgs>
    </configuration>
</plugin>
```

To build and run the projects, you can either use [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader), or any other JDK distribution.

## Preparation

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader).
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk)
    ```

2. Download or clone the repository and navigate into the `multithreading-demo/multithreading-demo-oversized_` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd multithreading-demo/multithreading-demo-oversized
    ```

## Multithreading Demo Oversized

1. Build the project:
    ```bash
    mvn package
    ```

2. Run the project on a JVM or as a native image:
    ```bash
    java -jar target/multithreading-1.0-jar-with-dependencies.jar
    ./target/multithreading-image-oversized
    ```

## Multithreading Demo Improved

Multithreading Demo Improved contains an enhanced version of the same program.

1. Change to the project directory:
    ```bash
    cd ..
    multithreading-demo-improved
    ```

2. Build the project:
    ```bash
    mvn package
    ```

3. Run the project on a JVM or as a native image:
    ```bash
    java -jar target/multithreading-1.0-jar-with-dependencies.jar
    ./target/multithreading-image-improved
    ```

### Learn More

Learn more about GraalVM tooling for Native Image on the [website](https://www.graalvm.org/latest/reference-manual/native-image/debugging-and-diagnostics/).