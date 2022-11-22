# GraalVM Demos: Native images for Faster Startup

This repository contains the code for a demo application for [GraalVM](http://graalvm.org).

## Prerequisites
* [GraalVM](http://graalvm.org)
* [Native Image](https://www.graalvm.org/docs/reference-manual/native-image/)

## Preparation

1. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$GRAALVM_HOME` and add `$GRAALVM_HOME/bin` to the `PATH` environment variable.

  On Linux:
  ```bash
  export GRAALVM_HOME=/home/${current_user}/path/to/graalvm
  export PATH=$GRAALVM_HOME/bin:$PATH
  ```
  On macOS:
  ```bash
  export GRAALVM_HOME=/Users/${current_user}/path/to/graalvm/Contents/Home
  export PATH=$GRAALVM_HOME/bin:$PATH
  ```
  On Windows:
  ```bash
  setx /M GRAALVM_HOME "C:\Progra~1\Java\<graalvm>"
  setx /M PATH "C:\Progra~1\Java\<graalvm>\bin;%PATH%"
  ```

2. Install [Native Image](https://www.graalvm.org/docs/reference-manual/native-image/#install-native-image) and `js` by running:
  ```bash
  gu install native-image
  ```

3. Download or clone the repository and navigate into the `native-image-logging-examples` directory:
  ```bash
  git clone https://github.com/graalvm/graalvm-demos
  cd graalvm-demos/native-image-logging-examples
  ```

There are two Java classes, one for the Build-Time Logger Initialization and second for Runtime Logger Initialization. The logger can be initialized at executable build time with a custom `logging.properties` configuration file, which is contained in the same directory as `BuildTimeLoggerInit.java` and `RuntimeLoggerInit.java`.

### Build-Time Logger Initialization

1. Compile `BuildTimeLoggerInit.java` using `javac`:

    ```bash
    javac BuildTimeLoggerInit.java
    ```
2. Build and run the native executable:

    ```bash
     native-image-logging-examples % native-image BuildTimeLoggerInit --initialize-at-build-time=BuildTimeLoggerInit
     ```
     ```bash
     ./buildtimeloggerinit
     ```

     It should produce output that looks similar to:
     ```bash
     WARNING: Danger, Will Robinson! [Wed May 18 17:22:40 BST 2022]
     ```

     Note that you can use any JDK for compiling the Java classes, but in the build script GraalVM's `javac` is used to simplify the prerequisites and not depend on you having another JDK installed.


### Runtime Logger Initialization

1. Compile `RuntimeLoggerInit.java` using `javac`:

     ```bash
     javac RuntimeLoggerInit.java
     ```

2. Build and run the native executable:
     ```bash
     native-image RuntimeLoggerInit -H:IncludeResources="logging.properties"
     ```
     ```bash
     ./runtimeloggerinit
     ```

    It should produce output that looks similar to:
     ```bash
     WARNING: Danger, Will Robinson! [Wed May 18 17:22:40 BST 2022]
     ```