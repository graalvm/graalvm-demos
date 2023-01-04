# Native Image Debugging from Java Source Code

This demo is intended to be used to demonstrate how to debug a Java application, built into a native executable, using [GraalVM VS Code Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=oracle-labs-graalvm.graalvm-pack).

## Prerequisites

1. Linux OS with GDB 10.1
2. Visual Studio Code
3. GraalVM Extension Pack for Java: Open Visual Studio Code, navigate to Extensions activity panel in the left-hand side Activity Bar (or use the Ctrl+Shift+X hot keys combination). Search for “GraalVM” in the search field. Find "GraalVM Extension Pack for Java", press Install. Reload will be required.
4. [GraalVM runtime environment in VS Code](https://www.graalvm.org/latest/tools/vscode/graalvm-extension/#graalvm-installation-wizard): Navigate to Gr activity panel in VS Code and install some of the latest GraalVM __Enterprise Edition__ versions available from the list.
5. [Native Image](https://www.graalvm.org/latest/reference-manual/native-image/): Upon GraalVM's installation completion, the “Install Optional GraalVM Components” window pops up in the right bottom corner. Install Native Image.

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
