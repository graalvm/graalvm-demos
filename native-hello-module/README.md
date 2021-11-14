### GraalVM Demos: Building a HelloWorld Java module into a native-image

#### Prerequisites
- [GraalVM](http://graalvm.org/) 
   - version 21.3.0 or higher (Java 11 or higher, Java 8 not supported, as the modules feature was added in Java 9)
- [Native Image](https://www.graalvm.org/docs/reference-manual/native-image/)

#### Preparation

This is a simple Java application, that demonstrates the use of modules in GraalVM and applying the `native-image` compiler to create a `native-image` from such modularised Java apps.

1. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$JAVA_HOME` and add `$JAVA_HOME/bin` to the `PATH` environment variable.

  On Linux:
  ```bash
  export JAVA_HOME=/home/${current_user}/path/to/graalvm
  export PATH=$JAVA_HOME/bin:$PATH
  ```
  On macOS:
  ```bash
  export JAVA_HOME=/Users/${current_user}/path/to/graalvm/Contents/Home
  export PATH=$JAVA_HOME/bin:$PATH
  ```
  On Windows:
  ```bash
  setx /M JAVA_HOME "C:\Progra~1\Java\<graalvm>"
  setx /M PATH "C:\Progra~1\Java\<graalvm>\bin;%PATH%"
  ```

2. Install [Native Image](https://www.graalvm.org/docs/reference-manual/native-image/#install-native-image) by running:
  ```bash
  gu install native-image
  ```

3. Download or clone the repository and navigate into the `native-hello-module` directory:
  ```bash
  git clone https://github.com/graalvm/graalvm-demos
  cd graalvm-demos/native-hello-module
  ```

#### Building the application

```bash
$ mvn package
```

A `target` folder with `HelloModule-1.0-SNAPSHOT.jar` will be created.

#### Run the application

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

    $ time java --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule
    Hello from Java Module: HelloModule


With GraalVM 21.3 we can **now also build Java modules** into images. Using:

    $ native-image --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule

    [hellomodule:10847]    classlist:     513.59 ms,  0.96 GB
    [hellomodule:10847]        (cap):     386.65 ms,  0.96 GB
    ....
    [hellomodule:10847]        image:     968.93 ms,  4.85 GB
    [hellomodule:10847]        write:     201.81 ms,  4.85 GB
    [hellomodule:10847]      [total]:  19,216.19 ms,  4.85 GB
                                          
builds the modular Java app into an image that can be executed with:

    $ ./hellomodule 
    Hello from Java Module: HelloModule

    $ time ./hellomodule 
    Hello from Java Module: HelloModule
