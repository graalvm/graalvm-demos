# Isolates for GraalVM Native Images

This application demonstrates the use of _isolates_ with [GraalVM Native Image](https://www.graalvm.org/docs/reference-manual/native-image/).
The code implements a web service that renders plots of mathematical functions, such as _sin(x)_.
For each requested plot, the web service spawns an isolate, which is an independent execution context.
The isolate generates the plot, and after it returns the result to the web service, it is discarded as a whole, along with any memory it used.

The code of this repository is based on an [earlier example](https://github.com/cstancu/netty-native-demo) for using `native-image` to build a web server using the [Netty](http://netty.io/) framework.
This example further uses the [exp4j](https://www.objecthunter.net/exp4j/) library to parse mathematical expressions, and [JFreeSVG](http://www.jfree.org/jfreesvg/) to generate SVG images.

## Preparation

To set up your development environment, you first need to [download](http://www.graalvm.org/downloads/) and set up GraalVM.
GraalVM Enterprise Edition is preferable for the purpose of this example.

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

For compilation, the `native-image` depends on the local toolchain.
Please make sure that `glibc-devel`, `zlib-devel` (header files for the C library and zlib), and `gcc` are available on your system. Some Linux distributions may additionally require `libstdc++-static`.
See [Prerequisites for Native Image](https://www.graalvm.org/reference-manual/native-image/#prerequisites).

## Build the project

The example is built with Maven:

```
mvn package
```

This creates a JAR file with all dependencies embedded: `target/netty-plot-0.1-jar-with-dependencies.jar`.

The [pom.xml](pom.xml) specifies the expected dependencies to Netty, emp4j, and JFreeSVG. It also specifies a dependency on Native Image, `org.graalvm.nativeimage`, - a technology to ahead-of-time compile Java code to a standalone executable. This dependency is required for the [method substitutions that are necessary for Netty](https://github.com/cstancu/netty-native-demo).

## Generate a native image

If the application is expected to use some dynamic features at runtime (e.g., Reflection, Java Native Interface, Class Path Resources), they have to be provided to the native image builder in the form of configuration files.
To avoid writing the configuration file yourself, apply the [tracing agent](https://www.graalvm.org/docs/reference-manual/native-image/#tracing-agent) when running on the Java HotSpot VM. It will observe the application behavior and create configuration files (jni-config.json, reflect-config.json, proxy-config.json and resource-config.json) in the  META-INF/native-image/ directory on the class path. The *reflect-config.json* file specifies classes which must be available via Java reflection at runtime.

```
java -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image -jar target/netty-plot-0.1-jar-with-dependencies.jar
```

Build a native image. The `native-image` builder will automatically search for any configuration file under _META-INF/native-image_ and its subdirectories:
```
native-image -jar target/netty-plot-0.1-jar-with-dependencies.jar
[netty-plot:16293]    classlist:   3,867.16 ms,  0.96 GB
[netty-plot:16293]        (cap):   1,496.45 ms,  0.96 GB
[netty-plot:16293]        setup:   6,442.19 ms,  0.96 GB
[netty-plot:16293]     (clinit):   1,332.53 ms,  2.29 GB
[netty-plot:16293]   (typeflow):  17,464.43 ms,  2.29 GB
[netty-plot:16293]    (objects):  11,776.77 ms,  2.29 GB
[netty-plot:16293]   (features):     953.49 ms,  2.29 GB
[netty-plot:16293]     analysis:  32,456.63 ms,  2.29 GB
[netty-plot:16293]     universe:   1,667.90 ms,  2.29 GB
[netty-plot:16293]      (parse):   8,029.35 ms,  2.29 GB
[netty-plot:16293]     (inline):   5,075.75 ms,  2.29 GB
[netty-plot:16293]    (compile):  96,649.45 ms,  4.74 GB
[netty-plot:16293]      compile: 112,232.42 ms,  4.45 GB
[netty-plot:16293]        image:   3,262.36 ms,  4.45 GB
[netty-plot:16293]        write:     683.96 ms,  4.45 GB
[netty-plot:16293]      [total]: 160,975.43 ms,  4.45 GB
```

The result is an executable file that is around 16 MByte in size:
```
du -h netty-plot
  16M    netty-plot
```

You can now run the executable:
```
./netty-plot
Open your web browser and navigate to http://127.0.0.1:8080/
```

Finally, you can open your browser and request rendering of a function, for example, by browsing to `http://127.0.0.1:8080/?function=abs((x-31.4)sin(x-pi/2))&xmin=0&xmax=31.4`.

### Background information

Instead of specifying any additional parameters on the command line, they may provided in a properties file in the input JAR file.
The `native-image` builder automatically looks for files named `native-image.properties` and for any other configuration file under `META-INF/native-image` including subdirectories, and processes their contents.
The tracing agent writes the *reflect-config.json* file specifying classes which must be available via Java reflection at runtime.

With Maven projects, the path convention is `META-INF/native-image/${groupId}/${artifactId}/native-image.properties`. In this example, the `META-INF/native-image/com.oracle.substratevm/netty-plot/native-image.properties` file contains the following:
```
ImageName = netty-plot
Args = --allow-incomplete-classpath
```
The `ImageName` property specifies the name of the resulting executable, while `Args` are treated like additional command-line arguments.

### A note about the application

This example cannot run as a regular Java application (on the JVM) and it cannot be profiled.
It will fail because the program tries to create an [isolate which is a native image specific feature](https://medium.com/graalvm/isolates-and-compressed-references-more-flexible-and-efficient-memory-management-for-graalvm-a044cc50b67e).
