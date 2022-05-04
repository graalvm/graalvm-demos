# GraalVM Demos: Hello Graal

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

1. Download and [install GraalVM](https://www.graalvm.org/docs/getting-started/#install-graalvm). You can download the latest GraalVM [here](https://www.graalvm.org/downloads/).

2. Put GraalVM on the `PATH`:

  ```bash
  export PATH=path/to/graal/bin:$PATH
  java -version
  ```
  On Oracle Linux or Red Hat:
  ```bash
  sudo yum -y install graalvm22-ee-<11 or 17>-<version>.el7.x86_64
  sudo yum -y install graalvm22-ee-<11 or 17-native-image
  java -version
  ```

  On a Debian based Linux machine:
  ```bash
  export GRAAL_ZIP=graalvm-ce-java<11 or 17-linux-amd64-<version>.tar.gz
  wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-<version>/$GRAAL_ZIP
  tar -zxvf $GRAAL_ZIP
  rm $GRAAL_ZIP
  export PATH=/root/graalvm-ce-java<11 or 17-<version>/bin:$PATH
  java -version
  gu install native-image
  ```
  
3. Download or clone the repository and navigate into the `functionGraphDemo` directory:
  ```bash
  git clone https://github.com/graalvm/graalvm-demos
  cd HelloGraal
  ```

## Running the Application from a Class File

1. Compile the application running the follow command:

  ```bash
  javac -d build src/com/hello/Graal.java
  ```
  This generates the `Graal.class` file into `build/com/hello` directory.

2. Run the application from a class file:

  ```bash
  java -cp ./build com.hello.Graal
  ```
  It outputs the message "hello graal".

## Running the Application from JAR

1. reate a JAR for the application, running the follow command:

  ```bash
  jar cfvm Hello.jar manifest.txt -C build .
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
  java -jar Hello.jar
  ```
  It outputs the message "hello graal".

## Running the Application as a Native Executable

1. Create a native executable of a JAR file:

  ```bash
  native-image -jar Hello.jar
  ```
  The executable called `./Hello` will be created in the working directory.

2. Execute it:
  ```bash
  ./Hello
  ```
  It outputs the message "hello graal".
  To check the filesize of the executable image, run: `ls -lh Hello`.

# References

1. [Creating a JAR File](https://docs.oracle.com/javase/tutorial/deployment/jar/build.html)

1. [Setting an Application's Entry Point](http://docs.oracle.com/javase/tutorial/deployment/jar/appman.html)

1. [How to create a Native Image](https://www.graalvm.org/reference-manual/native-image)
