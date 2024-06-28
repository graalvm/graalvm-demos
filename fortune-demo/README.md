# Native Image Fortune Demo

The demo is a Java program that simulates the traditional `fortune` Unix program (for more information, see [fortune](https://en.wikipedia.org/wiki/Fortune_(Unix))). The data for the fortune phrases is provided by [YourFortune](https://github.com/your-fortune).

The Fortune demo is comprised of three sub-projects: 
- The _fortune-maven_ project uses the [Maven plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html).
- The _fortune-gradle_ project uses the [Gradle plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html).
- The _staticfortune_ project demonstrates how to build a statically linked native executable using the [Maven plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html).

## Preparation

1. Download and install GraalVM. The easiest way is with [SDKMAN!](https://sdkman.io/jdks#graal):
    ```bash
    sdk install java <version>-graal
    ```
    For other installation options, visit the [Downloads](https://www.graalvm.org/downloads/) section.

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

> Requires GraalVM for JDK 21 for compatibility with Gradle. See [Gradle Java Compatibility Matrix](https://docs.gradle.org/current/userguide/compatibility.html).

1. Change to the _fortune-demo/fortune-gradle_ directory:
    ```bash
    cd ../fortune-gradle
    ```

2. Create a native executable using the [Gradle plugin for Native Image](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html):
    ```bash
    ./gradlew nativeCompile
    ```
    When the command completes, a native executable, `fortune`, is generated in the _build/native/nativeCompile_ directory of the project and ready for use.

3. Run the application from the native executable:
    ```bash
    ./fortune/build/native/nativeCompile/fortune
    ```

### Learn More

- [Native Image and Static Linking](https://www.graalvm.org/latest/reference-manual/native-image/guides/build-static-executables/)
- [Native Build Tools](https://graalvm.github.io/native-build-tools/latest/index.html)