# Native Image Multithreading Demo

The demo is a Java program which does synchronous and asynchronous threads execution.
Each thread loops through exactly the same array of integers and generates a stream of pseudorandom numbers.
The program calculates the time taken to perform the task synchronously and asynchronously.

Multithreading demo is comprised of two sub-projects, each built with Maven: **Multithreading Demo Oversized** and **Multithreading Demo Improved**.
The _pom.xml_ file of each sub-project includes the [Maven plugin for Native Image building](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html), which instructs Maven to generate a native executable of a JAR file with all dependencies at the `mvn package` step.

The plugin also generates a report using the [Native Image Build Reports](https://www.graalvm.org/latest/reference-manual/native-image/overview/Options/#build-output-and-build-report) feature in the _target_ directory with useful visualizations and comprehensive insights into different metrics of the native executable and the build process itself.

```xml
<plugin>
    <groupId>org.graalvm.buildtools</groupId>
    <artifactId>native-maven-plugin</artifactId>
    <version>${org.graalvm.buildtools.version}</version>
    <extensions>true</extensions>
    <executions>
        <execution>
            <id>native</id>
            <goals>
                <goal>compile-no-fork</goal>
            </goals>
            <phase>package</phase>
        </execution>
    </executions>
    <configuration>
        <imageName>${imageName}</imageName>
        <fallback>false</fallback>
        <buildArgs>
            <buildArg>
            -H:+UnlockExperimentalVMOptions -H:+BuildReport --initialize-at-build-time
            </buildArg>
        </buildArgs>
        <agent>
            <enabled>true</enabled>
            <defaultMode>Standard</defaultMode>
        </agent>
    </configuration>
</plugin>
```

## Preparation

1. Download and install the latest GraalVM JDK using [SDKMAN!](https://sdkman.io/).
    ```bash
    sdk install java 21.0.1-graal
    ```

2. Download or clone the repository and navigate into the `multithreading-demo/multithreading-demo-oversized_` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/archive/multithreading-demo/multithreading-demo-oversized
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

Continue reading about how you can inspect a profile and potentially optimize size of your native application using [Build Reports](https://www.graalvm.org/latest/reference-manual/native-image/guides/optimize-native-executable-size-using-build-report/).