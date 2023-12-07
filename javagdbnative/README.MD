# Native Image Debugging from Java Source Code

This demo is intended to be used to demonstrate how to debug a Java application, built into a native executable, using the [GraalVM Tools for Java extension for Visual Studio Code](https://marketplace.visualstudio.com/items?itemName=oracle-labs-graalvm.graalvm).

>Note: The extension is no longer in active development and is in maintenance mode. The latest version of the documentation that includes the GraalVM Tools for Java extension support is [GraalVM for JDK 17 (23.0.0)](https://www.graalvm.org/jdk17/tools/vscode/graalvm-extension/).

## Prerequisites

1. Linux OS with GDB 10.1
2. Visual Studio Code
3. [GraalVM Tools for Java](https://marketplace.visualstudio.com/items?itemName=oracle-labs-graalvm.graalvm): Open Visual Studio Code, navigate to Extensions activity panel in the left-hand side Activity Bar (or use the Ctrl+Shift+X hot keys combination). Search for “GraalVM” in the search field. Find "GraalVM Tools for Java", press Install. Reload will be required.
4. [GraalVM runtime environment in VS Code](https://www.graalvm.org/jdk17/tools/vscode/graalvm-extension/#set-default-java-runtime): Navigate to Gr activity panel in VS Code and install some of the latest GraalVM __Enterprise Edition__ versions available from the list.
5. [Native Image](https://www.graalvm.org/jdk17/tools/vscode/graalvm-extension/#native-image-support): Upon GraalVM's installation completion, the “Install Optional GraalVM Components” window pops up in the right bottom corner. Install Native Image.

## Building the Sample

1. Clone Download or clone the GraalVM Demos repository:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```

2. Open the demo folder, _graalvm/demos/javagdbnative_, in VS Code.

3. Open VS Code Terminal window and run following command:
    ```shell
    mvn -Pnative -DskipTests package
    ```

The `mvn -Pnative -DskipTests package` command will package a Java application into a runnable JAR file, and then build a native executable of it.

## Debugging the Sample

1. Select Run and Debug activity panel in VSCode
2. Add a new launch configuration named **Native Image: launch** into launch.json which should look like this:
    ```json
    {
        "type": "nativeimage",
        "request": "launch",
        "name": "Launch Native Image",
        "nativeImagePath": "${workspaceFolder}/target/javagdb",
        "args": "100"
    }
    ```
3. Then run debugger using **Launch Native Image** from **RUN...** menu. It will start debugging a native image binary in VSCode using Java source code.        


### References

- [GraalVM Tools for Java](https://www.graalvm.org/jdk17/tools/vscode/graalvm-extension/)
- [Native Image Debug Info Feature](https://www.graalvm.org/jdk17/reference-manual/native-image/debugging-and-diagnostics/DebugInfo/)