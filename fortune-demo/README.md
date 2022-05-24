# Java Fortune Demo For GraalVM Dashboard

The Fortune demo is provided to showcase the applicability of [GraalVM Dashboard](https://www.graalvm.org/docs/tools/dashboard/) -- a web-based tool that visualizes the composition of a native executable. (For more information, see [Native Image](https://www.graalvm.org/reference-manual/native-image/)).

The demo is a Java program that simulates the traditional `fortune` Unix program (for more information, see [fortune](https://en.wikipedia.org/wiki/Fortune_(Unix))). The data for the fortune phrases is provided by [YourFortune](https://github.com/your-fortune).

The Fortune demo is comprised of two sub-projects, each built with Maven: Fortune and StaticFortune.
The _pom.xml_ file of each sub-project includes the [Native Image Maven plugin](https://www.graalvm.org/reference-manual/native-image/NativeImageMavenPlugin/), which instructs Maven to generate a native executable from a JAR file with all dependencies at the `mvn package` step.
The plugin will also gather the diagnostic data at build time and write it to a dump file in the _target_ directory.

To build and run the projects, use [GraalVM](https://www.graalvm.org/downloads/).

## Fortune

1. Download or clone the repository and navigate into the _fortune-demo/fortune_ directory:
    ```
    git clone https://github.com/graalvm/graalvm-demos
    cd fortune-demo/fortune
    ```
2. Build the project:
    ```
    mvn package
    ```
3. Run the application as a native executable:
    ```
    ./target/fortune
    ```

## StaticFortune

The StaticFortune project contains an enhanced version of the same application.

1. Change to the project directory:
    ```
    cd ../staticfortune
    ```
2. Build the project:
    ```
    mvn package
    ```
3. Run the project as a native executable:
    ```
    ./target/staticfortune
    ```
