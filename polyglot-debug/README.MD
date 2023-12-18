# Debugging a Java with JavaScript Embedding Application

This sample code helps to demonstrate simplicity of debugging and developing GraalVM polyglot applications using Visual Studio (VS) Code extensions.

## Prerequisites

1. Install VS Code and launch it.
2. Then go to VS Code Extensions activity panel and search for _GraalVM Extension Pack_, install it. The GraalVM Extension Pack will install all required extensions.
2. Open Visual Studio Code, navigate to Extensions activity panel in the left-hand side Activity Bar. Search for “GraalVM” in the search field. Find  [GraalVM Tools for Java](https://marketplace.visualstudio.com/items?itemName=oracle-labs-graalvm.graalvm), press Install. Reload if required.
3. [Install GraalVM in VS Code](https://www.graalvm.org/latest/tools/vscode/graalvm-extension/#graalvm-installation-wizard): Navigate to Gr activity panel in VS Code and install the latest Oracle GraalVM version available from the list.
4. Confirm to set this as _Active GraalVM_ for VS Code.

## Build and Run the Application

1. Open VS Code Terminal. 
2. Clone the repository and navigate into the `polyglot-debug` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/polyglot-debug
    ```

3. Build the application using Maven:
    ```bash
    mvn clean package
    ```
4. Run this application with the following command:
    ```bash
    mvn exec:exec
    ```

## Debug the Application

Debugging a polyglot application should work out of the box in VS Code with the GraalVM extension.

1. Place breakpoints in _PolyglotDebug.java_.

2. To debug the project, go to __Run and Debug__ activity or simply press F5.

3. Choose **Launch Java 8+** as this is the Polyglot debugger provided by the GraalVM extension.

That is, the sample is running using the GraalVM debugger. 
