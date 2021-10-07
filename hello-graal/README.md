# GraalVM Demos: Hello Graal

This is a "Hello Graal" Java example for GraalVM.
The structure of the ``Hello`` package is like this:

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

* Enterprise Edition

1. Download [GraalVM Enterprise](https://www.oracle.com/downloads/graalvm-downloads.html).
Unzip the archive file. For example, on Linux or macOS run the following:
  ```bash
  tar -zxvf graalvm-ee-java11-linux-amd64-<version>.tar.gz && rm graalvm-ee-java11-linux-amd64-<version>.tar.gz
  ```
  
2. Put GraalVM on the `PATH`:
  ```bash
  export PATH=path/to/graal/bin:$PATH
  java -version
  ```

* Community Edition

On Oracle Linux or Red Hat:
```bash
sudo yum -y install graalvm21-ee-11-<version>.el7.x86_64
sudo yum -y install graalvm21-ee-11-native-image
java -version
```

On a Debian based Linux machine:
```bash
export GRAAL_ZIP=graalvm-ce-java11-linux-amd64-<version>.tar.gz
wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-21.0.0.2/$GRAAL_ZIP
tar -zxvf $GRAAL_ZIP
rm $GRAAL_ZIP
export PATH=/root/graalvm-ce-java11-<version>/bin:$PATH
java -version
gu install native-image
```

## Preparation

Download or clone the examples repository:
```bash
git clone https://github.com/chrisbensen/HelloGraal
cd HelloGraal
```

## Compile the code

To compile the main class, run the follow command:
```bash
javac -d build src/com/hello/Graal.java
```
This generates the `Graal.class` file into `build/com/hello` directory.

## Run the class

To run the main class, run the follow command:
```bash
java -cp ./build com.hello.Graal
```
It outputs the message "hello graal".

## Create a JAR file

Create a JAR for the application, run the follow command:
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

## Run a JAR file

Run the JAR file:
```bash
java -jar Hello.jar
```

It outputs the message "hello graal".

## Create a native image

Create a native executable of a JAR file:
```bash
native-image -jar Hello.jar
```
The executable called `./Hello` will be created in the working directory.
Execute it:
```bash
./Hello
```
It outputs the message "hello graal". To check the filesize of the executable image, run: `ls -lh Hello`.

# References

1. [Creating a JAR File](https://docs.oracle.com/javase/tutorial/deployment/jar/build.html)

1. [Setting an Application's Entry Point](http://docs.oracle.com/javase/tutorial/deployment/jar/appman.html)

1. [How to create a Native Image](https://www.graalvm.org/reference-manual/native-image)
