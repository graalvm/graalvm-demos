# Build a Layered Native Executable Using the `native-image` Tool

This is a HelloWorld Java example, referenced from [Getting Started with Native Image](https://www.graalvm.org/latest/reference-manual/native-image/), using the [GraalVM Native Image Layers](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/imagelayer/NativeImageLayers.md) feature.

### Prerequisites

- Linux x64
- Latest GraalVM 25.1 EA build (with Native Image support)

Native Image Layers is an experimental feature. For the best experience, use the latest [GraalVM Early Access Build](https://github.com/graalvm/oracle-graalvm-ea-builds/releases).

## Environment Setup
Point your `JAVA_HOME` to the GraalVM distribution.
```bash
export JAVA_HOME=/path/to/graalvm/ea/build
```

## Run the Application from a Class File

1. Compile the application by running the following command:
    ```bash
    javac -d build src/com/example/HelloWorld.java
    ```
    This command generates the _HelloWorld.class_ file in the _build/com/example_ directory.

2. Run the application from a class file:
    ```bash
    java -cp ./build com.example.HelloWorld
    ```
    It outputs the message "Hello, Native World!".

## Run the Application from JAR

1. Create a JAR for the application, running the follow command:
    ```bash
    jar --create --file HelloWorld.jar --main-class com.example.HelloWorld -C build .
    ```
2. Run the JAR file:
    ```bash
    java -jar HelloWorld.jar
    ```

## Run the Application as a Native Executable

1. Create a base layer that contains `java.base`:
    ```bash
    mkdir -p native-build
    native-image -H:LayerCreate=baselayer.nil,module=java.base -cp ./build -o libjavabaselayer -H:Path=./native-build
    ```
    For more details consult the [Native Image Layers documentation](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/imagelayer/NativeImageLayers.md).

This command creates:

* _base-layer.nil_, which is a build-time dependency for the application build.
* _libjavabaselayer.so_, which is a runtime dependency for the application layer.
    It will also create the `libjavabaselayer.so` shared library which is a run time dependency for the application layer.

2. Create a native executable from a JAR file using the base layer:
    ```bash
    native-image -H:LayerUse=native-build/libjavabaselayer.nil -cp ./build -H:LinkerRPath="\$ORIGIN/native-build" -jar HelloWorld.jar
    ```
    The executable called `./HelloWorld` will be created in the working directory.

3. Execute it:
    ```bash
    ./HelloWorld
    ```