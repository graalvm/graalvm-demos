# Debugging a Java with JavaScript Embedding Application

GraalVM supports debugging of [polyglot applications](https://www.graalvm.org/latest/reference-manual/polyglot-programming/).
You can attach compatible debuggers such as Chrome Developer Tools to a process running on GraalVM.
To debug guest language applications, pass the `--inspect` option to the command line launcher.

## Prerequisites

Download and install the GraalVM JDK using [SDKMAN!](https://sdkman.io/).
```bash
sdk install java 21.0.1-graal
```
For other installation options, visit the [Downloads page](https://www.graalvm.org/downloads/).

## Build and Run the Application

2. Clone the repository and navigate into the `polyglot-debug` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/archive/polyglot-debug
    ```
3. Build the application using Maven:
    ```bash
    mvn clean package
    ```
4. Run this application with the following command:
    ```bash
    mvn exec:exec
    ```

Learn more about [debugging with Chrome Debugger](https://www.graalvm.org/latest/tools/chrome-debugger/) at the website.