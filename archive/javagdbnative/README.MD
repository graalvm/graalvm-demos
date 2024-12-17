# Native Image Debugging from Java Source Code

This demo is intended to demonstrate how to debug a Java application, built into a native executable, using the [Debug Info Feature in Native Image](https://www.graalvm.org/latest/reference-manual/native-image/debugging-and-diagnostics/DebugInfo/).

> Prerequisites: Linux OS with 10.2 or later

## Build a Native Executable with Debug Information

1. Clone Download or clone the GraalVM Demos repository:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```

2. Navigate to the example directory:
    ```bash
    cd graalvm-demos/archive/javagdbnative
    ```

3. Compile and build the application with Maven:
    ```shell
    mvn -Pnative -DskipTests package
    ```
    The command packages a Java application into a runnable JAR file, and then builds a native executable of it.
    The `-g` option that is passed to the `native-image` tool in _pom.xml_ causes debug information to be generated.
    Next to the native executable, you see the _<executable_name>.debug_ file that contains debug information, and the _sources_ directory containing Java source files, which the debugger uses to show sources for line information.

Now you can debug your native executable with GDB as shown in [this guide](https://www.graalvm.org/latest/reference-manual/native-image/guides/debug-native-image-process/).