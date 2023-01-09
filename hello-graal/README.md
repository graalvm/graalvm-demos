# Hello Graal Java Example

This is a "Hello Graal" Java example for GraalVM. The structure of the `Hello` package is like this:

  ```
  .
  | src
  |   `--com/
  |      `-- hello
  |          `-- Graal.java
  |-- LICENSE
  |-- .gitignore
  |-- manifest.txt
  `-- README.md
  ```
  
## Preparation

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader). 
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) 
    ```
    
2. Download or clone GraalVM demos repository and navigate into the `hello-graal` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/hello-graal
    ```

## Running the Application from a Class File

1. Compile the application running the follow command:
    ```bash
    $JAVA_HOME/bin/javac -d build src/com/hello/Graal.java
    ```
    This generates the `Graal.class` file into `build/com/hello` directory.

2. Run the application from a class file:
    ```bash
    $JAVA_HOME/bin/java -cp ./build com.hello.Graal
    ```
    It outputs the message "hello graal".

## Running the Application from JAR

1. Create a JAR for the application, running the follow command:
    ```bash
    $JAVA_HOME/bin/jar cfvm Hello.jar manifest.txt -C build .
    jar tf Hello.jar
    ```
    The output will be:
    ```bash
    META-INF/
    META-INF/MANIFEST.MF
    com/
    com/hello/
    com/hello/Graal.class
    ```

2. Run the JAR file:
    ```bash
    $JAVA_HOME/bin/java -jar Hello.jar
    ```
    It outputs the message "hello graal".

## Running the Application as a Native Executable

1. Create a native executable of a JAR file:
    ```bash
    $JAVA_HOME/bin/native-image -jar Hello.jar
    ```
    The executable called `./Hello` will be created in the working directory.

2. Execute it:
    ```bash
    ./hello
    ```
    It outputs the message "hello graal".
    To check the filesize of the executable image, run: `ls -lh Hello`.

### References

1. [Creating a JAR File](https://docs.oracle.com/javase/tutorial/deployment/jar/build.html)
2. [Setting an Application's Entry Point](http://docs.oracle.com/javase/tutorial/deployment/jar/appman.html)
3. [How to create a Native Image](https://www.graalvm.org/latest/reference-manual/native-image/)