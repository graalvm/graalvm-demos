# Build a Native Executable Using the `native-image` Tool

This is a HelloWorld Java example, referenced from [Getting Started with Native Image](https://www.graalvm.org/latest/reference-manual/native-image/).
  
## Run the Application from a Class File

1. Compile the application running the follow command:
    ```bash
    javac -d build src/com/example/HelloWorld.java
    ```
    This generates the `HelloWorld.class` file into `build/com/example` directory.

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

1. Create a native executable of a JAR file:
    ```bash
    native-image -jar HelloWorld.jar
    ```
    The executable called `./HelloWorld` will be created in the working directory.

2. Execute it:
    ```bash
    ./HelloWorld
    ```