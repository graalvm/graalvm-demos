# Native Image Fortune Demo

The demo is a Java program that simulates the traditional `fortune` Unix program (for more information, see [fortune](https://en.wikipedia.org/wiki/Fortune_(Unix))). The data for the fortune phrases is provided by [YourFortune](https://github.com/your-fortune).

The Fortune demo is comprised of three sub-projects: 
- The _fortune-maven_ project uses the [Maven plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html).
- The _fortune-gradle_ project uses the [Gradle plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html).
- The _staticfortune_ project demonstrates how to build a statically linked native executable using the [Maven plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html).

This demo is a little more complicated than _HelloWorld_, and requires pre-configuration before building a native executable. The Maven and Gradle plugins for Native Image building can generate the required configuration for you by injecting the Java agent at package time.
The plugin will also gather the diagnostic data at build time and write it to a dump file in the `target` directory.

## Preparation

1. Download and install the latest GraalVM for JDK 21 using [SDKMAN!](https://sdkman.io/).
    ```bash
    sdk install java 21.0.3-graal
    ```
    
2. Download or clone GraalVM demos repository:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
   
## Fortune Maven Demo

1. Change to the _fortune-demo/fortune-maven_ directory:
    ```bash
    cd fortune-demo/fortune-maven
    ```

2. Build the project:
    ```bash
    mvn clean package
    ```

3. Run your application with the [agent](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html#agent-support), on the JVM:
    ```bash
    mvn -Pnative -Dagent exec:exec@java-agent
    ```
    The application will return a random saying. 
    The agent generates the configuration files in the _target/native/agent-output_ subdirectory.

4. Build a native executable of this application with GraalVM Native Image and Maven:
    ```bash
    mvn -Pnative -Dagent package
    ```
    When the command completes, a native executable, `fortune`, is generated in the _target_ directory of the project and ready for use.

5. Run the application by launching a native executable directly or with the Maven profile:

    ```bash
    ./target/fortune
    ```
    ```bash
    mvn -Pnative exec:exec@native
    ```

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

## StaticFortune

The StaticFortune project contains an enhanced version of the same application and uses the [Maven plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html).

1. Change to the project directory:
    ```bash
    cd ../staticfortune
    ```

2. Build the project:
    ```bash
    mvn clean package
    ```

3. Run your application with the [agent](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html#agent-support), on the JVM:
    ```bash
    mvn -Pnative -Dagent exec:exec@java-agent
    ```
    The application will print a random saying. 
    The agent generates the configuration files in the _target/native/agent-output_ subdirectory.

4. Build a static native executable:
    ```bash
    mvn -Pnative -Dagent package
    ```
    When the command completes, a native executable, `staticfortune`, is generated in the _target_ directory of the project and ready for use.

5. Run the demo by launching a native executable directly or with the Maven profile:
    ```bash
    ./target/staticfortune
    ```
    ```bash
    mvn -Pnative exec:exec@native
    ```

To see the benefits of executing these applications as native executables, time the execution and compare with running on the JVM.

### Learn More

- [Native Image and Static Linking](https://www.graalvm.org/latest/reference-manual/native-image/guides/build-static-executables/)
- [Native Build Tools](https://graalvm.github.io/native-build-tools/latest/index.html)