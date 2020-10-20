## Hello Graal

This is a "Hello Graal" Java example for GraalVM.

The structure of the ``Hello`` package is like this: ::

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

## Install GraalVM

Enterprise Edition

   Download [GraalVM Enterprise](https://www.oracle.com/downloads/graalvm-downloads.html)

   Unzip the download. For example on Linux run the following:

   ```
   tar -zxvf graalvm-ee-java11-linux-amd64-20.2.0.tar.gz && rm graalvm-ee-java11-linux-amd64-20.2.0.tar.gz
   ```

   Put GraalVM on the path:

   ```
   export PATH=path/to/graal/bin:$PATH
   java -version
   ```

Community Edition

   On Oracle Linux or Red Hat:
   ```
   sudo yum -y install graalvm20-ee-11-20.2.0-1.el7.x86_64
   sudo yum -y install graalvm20-ee-11-native-image
   java -version
   ```

   On a Debian based Linux computer:

   ```
   export GRAAL_ZIP=graalvm-ce-java11-linux-amd64-20.2.0.tar.gz
   wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-20.2.0/$GRAAL_ZIP
   tar -zxvf $GRAAL_ZIP
   rm $GRAAL_ZIP
   export PATH=/root/graalvm-ce-java11-20.2.0/bin:$PATH
   java -version
   gu install native-image
   ```

## Clone reposotory

   ```
   git clone https://github.com/chrisbensen/HelloGraal
   cd HelloGraal
   ```

## Compile the code

To compile the main class, run the follow command: ::

   ```
   javac -d build src/com/hello/Graal.java
   ```

This generates the ``Graal.class`` file into ``build/com/hello`` directory.

## Run the class

To run the main class, run the follow command: ::

   ```
   java -cp ./build com.hello.Graal
   ```

This shows the message ``hello graal``.

## Create a JAR file

Create a JAR for the application, run the follow command: ::

   ```
   jar cfvm Hello.jar manifest.txt -C build .

   jar tf Hello.jar
   ```

The output will be:

   ```
   META-INF/
   META-INF/MANIFEST.MF
   com/
   com/hello/
   com/hello/Graal.class
   ```

## Run a JAR file

Run the JAR file: ::

   ```
   java -jar Hello.jar
   ```

This shows the ``hello graal`` message.

## Create a native image

Run the native-image command: ::

   ```
   native-image -jar Hello.jar
   ```

# References

1. `Creating a JAR File [https://docs.oracle.com/javase/tutorial/deployment/jar/build.html]`.

1. `Setting an Application's Entry Point [http://docs.oracle.com/javase/tutorial/deployment/jar/appman.html]`.

1. `How to create a GraalVM native-image [https://www.graalvm.org/reference-manual/native-image]`.
