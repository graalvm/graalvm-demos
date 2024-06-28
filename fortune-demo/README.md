# Native Image Fortune Demo

The demo is a Java program that simulates the traditional `fortune` Unix program (for more information, see [fortune](https://en.wikipedia.org/wiki/Fortune_(Unix))). The data for the fortune phrases is provided by [YourFortune](https://github.com/your-fortune).

The Fortune demo is comprised of three sub-projects: 
- The _fortune-maven_ project uses the [Maven plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html).
- The _fortune-gradle_ project uses the [Gradle plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html).
- The _staticfortune_ project demonstrates how to build a statically linked native executable using the [Maven plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html).

## Preparation

1. Download and install GraalVM for JDK 23 or later using [SDKMAN!](https://sdkman.io/).
    ```bash
    sdk install java <version>-graal
    ```
    
2. Download or clone GraalVM demos repository:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
   
## Fortune Maven Demo

1. Create a native executable using the [Maven plugin for Native Image](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html):
    ```bash
    mvn -Pnative package
    ```
    The command compiles the project, creates a JAR file with all dependencies, and then generates a native executable, `fortune`, in the _target_ directory, ready for use.

2. Run the application:
    ```bash
    ./target/fortune
    ```
    The application will return a random saying.

## Fortune Gradle Demo

1. Change to the _fortune-demo/fortune-gradle_ directory:
    ```bash
    cd ../fortune-gradle
    ```

2. Build the project:
    ```bash
    ./gradlew run
    ```

3. Run your application with the [agent](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html#agent-support), on the JVM. To enable the agent with the Native Image Gradle plugin, pass the `-Pagent` option to any Gradle tasks that extends `JavaForkOptions`:
    ```bash
    ./gradlew -Pagent run
    ```
    The agent captures and records the dynamic features encountered during a test run into multiple *-config.json files.

4. Once the metadata is collected, copy it into the project’s _/META-INF/native-image/_ directory using the `metadataCopy` task:
    ```bash
    ./gradlew metadataCopy --task run --dir src/main/resources/META-INF/native-image
    ```

5. Build a native executable using configuration collected by the agent:
    ```bash
    ./gradlew nativeCompile
    ```

    When the command completes, a native executable, `fortune`, is generated in the _build/native/nativeCompile_ directory of the project and ready for use.
6. Run the application from the native executable:
    ```bash
    ./fortune/build/native/nativeCompile/fortune
    ```

### Learn More

- [Native Image and Static Linking](https://www.graalvm.org/latest/reference-manual/native-image/guides/build-static-executables/)
- [Native Build Tools](https://graalvm.github.io/native-build-tools/latest/index.html)