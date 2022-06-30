# GraalVM Demos: Building a HelloWorld Java Module into a Native Executable
This repository contains the code for a demo application for [GraalVM](http://graalvm.org).

## Prerequisites
* [GraalVM](http://graalvm.org)
* [Native Image](https://www.graalvm.org/docs/reference-manual/native-image/)
* Maven

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

2. Install [Native Image](https://www.graalvm.org/docs/reference-manual/native-image/#install-native-image) by running:
  ```bash
  gu install native-image
  ```
For compilation, the `native-image` tool depends on the local toolchain.
Please make sure that `glibc-devel`, `zlib-devel` (header files for the C library and zlib), and `gcc` are available on your system. Some Linux distributions may additionally require `libstdc++-static`.
See [Prerequisites for Native Image](https://www.graalvm.org/dev/reference-manual/native-image/#prerequisites).

3. Download or clone the repository and navigate into the `native-hello-module` directory:
  ```bash
  git clone https://github.com/graalvm/graalvm-demos
  cd graalvm-demos/native-hello-module
  ```
## Build the Application
Before running this example, you need to build the application:

```bash
$ mvn package
```
This creates a JAR file with all dependencies embedded: `target/HelloModule-1.0-SNAPSHOT.jar`

## Run the Application
The following modularized Java Hello World example

    ├── hello
    │   └── Main.java
    │       > package hello;
    │       > 
    │       > public class Main {
    │       >     public static void main(String[] args) {
    │       >         System.out.println("Hello from Java Module: "
    │       >             + Main.class.getModule().getName());
    │       >     }
    │       > }
    │
    └── module-info.java
        > module HelloModule {
        >     exports hello;
        > }

can be executed via Java using the following command:
    
    $ java --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule
    Hello from Java Module: HelloModule
    
With GraalVM you can **now build Java modules** into native executables, using the following syntax:

    $ native-image --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule

    [hellomodule:10847]    classlist:     513.59 ms,  0.96 GB
    [hellomodule:10847]        (cap):     386.65 ms,  0.96 GB
    ....
    [hellomodule:10847]        image:     968.93 ms,  4.85 GB
    [hellomodule:10847]        write:     201.81 ms,  4.85 GB
    [hellomodule:10847]      [total]:  19,216.19 ms,  4.85 GB
                                      
It builds the modular Java app into a native executable that can be executed with:
    
    $ ./hellomodule 
    Hello from Java Module: HelloModule
