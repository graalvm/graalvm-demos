# Use Gradle to Build a Java Application from a Java Application

The demo application simulates the traditional [fortune](https://en.wikipedia.org/wiki/Fortune_(Unix)) Unix program.
The data for the fortune phrases is provided by [YourFortune](https://github.com/your-fortune).

The project uses the [Gradle plugin for GraalVM Native Image building](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html).

## Run the Demo

1. Run the application on JVM:
    ```bash 
    ./gradlew run
    ```
2. Build a native executable:
    ```bash 
    ./gradlew nativeRun
    ```
    The `nativeRun` task compiles the application, invokes `nativeCompile`, and then runs the executable. 
3. Re-run the application from the native executable:
    ```bash
    ./build/native/nativeCompile/fortune
    ```
Alternatively, you can execute `./run.sh` to build and run the application.