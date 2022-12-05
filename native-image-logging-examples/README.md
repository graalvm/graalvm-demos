# Configuring Logging in Native Image

This demo demonstrates how the `java.util.logging.*` API can be used with Native Image. If your application require additional logging handlers, you can register them with a custom configuration file, _logging.properties_ , and initialize at build time. This reduces the size of the resulting executable file and improves the startup time. Unless your application needs to process _logging.properties_ at run time, this approach is recommended. Both approaches are demonstrated in the examples. 

### Prerequisites
* [GraalVM](http://graalvm.org)
* [Native Image](https://www.graalvm.org/docs/reference-manual/native-image/)

## Preparation

1. Download and install the latest GraalVM JDK with Native Image using [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
  ```bash
  bash <(curl -sL https://get.graalvm.org/jdk) -c 'native-image'
  ```

2. Download or clone the repository and navigate into the `native-image-logging-examples` directory:
  ```bash
  git clone https://github.com/graalvm/graalvm-demos
  cd graalvm-demos/native-image-logging-examples
  ```

There are two Java classes: one for the build-time logger initialization and the second for runtime logger initialization. The logger will be initialized with a custom _logging.properties_ configuration file, which is placed in the same directory as _LoggerBuildTimeInit_ and _LoggerRunTimeInit_.

## Initializing a Logger at Build Time

In this example, the logger will be initialized at build time with a custom _logging.properties_ configuration file, placed in the same repository as _LoggerBuildTimeInit_.

1. Compile _LoggerBuildTimeInit.java_ using `javac`:

    ```bash
    $JAVA_HOME/bin/javac LoggerBuildTimeInit.java
    ```
2. Build and run the native executable:

    ```bash
    $JAVA_HOME/bin/native-image LoggerBuildTimeInit --initialize-at-build-time=LoggerBuildTimeInit
    ```
    ```bash
    ./loggerbuildtimeinit
    ```

    It should produce the output that looks similar to:
    ```bash
    WARNING: Danger, Will Robinson! [Wed May 18 17:22:40 BST 2022]
    ```

The _logging.properties_ file is processed when the executable is built. `LoggerHolder.LOGGER` is initialized at build time and is available at runtime, therefore improving the startup time. Unless your application needs to process a custom _logging.properties_ configuration file at runtime, this approach is recommended.

## Initializing a Logger at Runtime

The logger can also be initialized at runtime. 

1. Compile _LoggerRunTimeInit_ using `javac`:

    ```bash
    $JAVA_HOME/bin/javac LoggerRunTimeInit.java
    ```

2. Build and run the native executable:
    ```bash
    $JAVA_HOME/bin/native-image LoggerRunTimeInit -H:IncludeResources="logging.properties"
    ```
    ```bash
    ./loggerruntimeinit 
    ```

    It should produce the output that looks similar to:
    ```bash
    WARNING: Danger, Will Robinson! [Wed May 18 17:22:40 BST 2022]
    ```

    In this case, the _logging.properties_ file needs to be available for runtime processing and it must be included in the executable via the `-H:IncludeResources=logging.properties` option. For more details, see [Use of Resources in a Native Executable](https://www.graalvm.org/reference-manual/native-image/dynamic-features/Resources/).

Native Image supports logging using the `java.util.logging.*` API.
The logging configuration by default is based on the _logging.properties_ file found in the JDK.
