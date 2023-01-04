# Java Fortune Demo For GraalVM Dashboard

The Fortune demo is provided to showcase the applicability of [GraalVM Dashboard](https://www.graalvm.org/docs/tools/dashboard/) - a web-based tool that visualizes the composition of a native executable. (For more information, see [Native Image](https://www.graalvm.org/reference-manual/native-image/)).

The demo is a Java program that simulates the traditional `fortune` Unix program (for more information, see [fortune](https://en.wikipedia.org/wiki/Fortune_(Unix))). The data for the fortune phrases is provided by [YourFortune](https://github.com/your-fortune).

The Fortune demo is comprised of two sub-projects, each built with Maven: Fortune and StaticFortune.
The `pom.xml` file of each sub-project includes the [Maven plugin from Native Image Build Tools](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html), which instructs Maven to generate a native executable from a JAR file with all dependencies.
This demo is a little more complicated than HelloWorld, and requires pre-configuration before building a native executable. The Native Image Maven plugin can generate the required configuration for you by injecting the Java agent at package time.
The plugin will also gather the diagnostic data at build time and write it to a dump file in the `target` directory.

## Preparation

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader).
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) 
    ```
2. Download or clone GraalVM demos repository:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
   
## Fortune
1. Navigate into the `fortune-demo/fortune` directory:
    ```bash
    cd fortune-demo/fortune
    ```

2. Build the project:
    ```bash
    mvn clean package
    ```
3. When the build succeeds, run the application on a JVM with the [Java agent](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html#agent-support). Since you have installed GraalVM, it will run on GraalVM JDK.
    ```bash
    mvn -Pnative -Dagent exec:exec@java-agent
    ```
    The application will return a random saying. 
    The agent generates the configuration files in the `target/native/agent-output` subdirectory.

4. Build a native executable of this application with GraalVM Native Image and Maven:
    ```bash
    mvn -Pnative -Dagent package
    ```
    When the command completes, a native executable, `fortune`, is generated in the `/target` directory of the project and ready for use.
    The diagnostic data for GraalVM Dashboard is written to a dump file `/target/fortune.bgv`.

5. Run the application by launching a native executable directly or with the Maven profile:

    ```bash
    ./target/fortune
    ```
    ```bash
    mvn -Pnative exec:exec@native
    ```

## StaticFortune

The StaticFortune project contains an enhanced version of the same application.

1. Change to the project directory:
    ```bash
    cd ../staticfortune
    ```
2. Build the project:
    ```bash
    mvn clean package
    ```

3. Run the application on a JVM (GraalVM JDK) with the [Java agent](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html#agent-support):
    ```bash
    mvn -Pnative -Dagent exec:exec@java-agent
    ```
    The application will print a random saying. 
    The agent generates the configuration files in the `target/native/agent-output` subdirectory.
4. Build a static native executable:
    ```bash
    mvn -Pnative -Dagent package
    ```
    When the command completes, a native executable, `staticfortune`, is generated in the `/target` directory of the project and ready for use.
    The diagnostic data for GraalVM Dashboard is written to a dump file `/target/staticfortune.bgv`.
5. Run the demo by launching a native executable directly or with the Maven profile:
    ```bash
    ./target/staticfortune
    ```
    ```bash
    mvn -Pnative exec:exec@native
    ```

To see the benefits of executing these applications as native executables, time the execution and compare with running on the JVM.

### Learn More

Learn more about Native Image and static linking [here](https://www.graalvm.org/latest/reference-manual/native-image/guides/build-static-executables/).