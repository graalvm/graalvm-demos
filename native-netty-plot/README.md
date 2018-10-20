# Isolates for GraalVM Native Images

This repository demonstrates the use of _isolates_ with the [GraalVM `native-image`](http://www.graalvm.org/docs/reference-manual/aot-compilation/) tool. The code implements a web service that renders plots of mathematical functions, such as _sin(x)_. For each requested plot, the web service spawns an isolate, which is an independent execution context. The isolate generates the plot, and after it returns the result to the web service, it is discarded as a whole, along with any memory it used.

The code of this repository is based on an [earlier example](https://github.com/cstancu/netty-native-demo) for using `native-image` to build a web server using the [Netty](http://netty.io/) framework. This example further uses the [exp4j](https://www.objecthunter.net/exp4j/) library to parse mathematical expressions, and [JFreeSVG](http://www.jfree.org/jfreesvg/) to generate SVG images.

### Preparing the environment

To set up your development environment, you first need to [download GraalVM](http://www.graalvm.org/downloads/). The Enterprise Edition is preferable for the purpose of this example. The GraalVM download contains a full Java VM, the `native-image` tool, and other utilities. We assume that you extracted GraalVM to `~/graalvm-ee-1.0.0-rc8`.

Now you can run the `native-image` tool:
```
$ ~/graalvm-ee-1.0.0-rc8/native-image --help

GraalVM native-image building tool

This tool can be used to generate an image that contains ahead-of-time compiled Java code.
...
```

Alternatively, you can build `native-image` from source by following the [quick start guide](https://github.com/oracle/graal/tree/master/substratevm#quick-start).

`native-image` needs a local C toolchain, so please make sure that `glibc-devel`, `zlib-devel` (header files for the C library and `zlib`) and `gcc` are available on your system. On Ubuntu, the following command may be required to install `zlib-devel`, the rest of the dependencies should come preinstalled:
```
$ sudo apt-get install zlib1g-dev
```

### Building the source code with Maven

The example is built with Maven:

```
$ mvn package
```

This creates a .jar file with all dependencies embedded: `target/netty-plot-0.1-jar-with-dependencies.jar`.

The [pom.xml](pom.xml) specifies the expected depenencies to Netty, emp4j, and JFreeSVG. But it also specifies a dependency on Substrate VM (the technology behind native images). This depenency is necessary to make the native image API and also core classes and annotations of Substrate VM available. This is required for the [method substitutions that are necessary for Netty](https://github.com/cstancu/netty-native-demo).

### Building a GraalVM Native Image

To build the native image, use the `native-image` tool:
```
$ ~/graalvm-ee-1.0.0-rc8/bin/native-image -jar target/netty-plot-0.1-jar-with-dependencies.jar
Build on Server(pid: 8610, port: 32890)
[netty-plot:8610]    classlist:     854.40 ms
[netty-plot:8610]        (cap):     608.64 ms
[netty-plot:8610]        setup:     956.98 ms
[netty-plot:8610]   (typeflow):   4,908.30 ms
[netty-plot:8610]    (objects):   4,946.27 ms
[netty-plot:8610]   (features):     103.51 ms
[netty-plot:8610]     analysis:  10,150.14 ms
[netty-plot:8610]     universe:     250.36 ms
[netty-plot:8610]      (parse):     624.32 ms
[netty-plot:8610]     (inline):     644.10 ms
[netty-plot:8610]    (compile):   6,692.94 ms
[netty-plot:8610]      compile:   8,521.66 ms
[netty-plot:8610]        image:     844.66 ms
[netty-plot:8610]        write:     153.73 ms
[netty-plot:8610]      [total]:  21,764.10 ms
```
The result is an executable file that is around 8 MByte in size:
```
$ du -h netty-plot
  8.2M    netty-plot
```
You can now run the executable:
```
$ ./netty-plot
Open your web browser and navigate to http://127.0.0.1:8080/
```

Finally, you can open your browser and request the rendering of a function, for example by browsing to http://127.0.0.1:8080/?function=abs((x-31.4)sin(x-pi/2))&xmin=0&xmax=31.4

##### Build Parameters
This build requires additional parameters to `native-image`, most prominently `-H:+SpawnIsolates` to enable support for isolates, and `--features=...` to enable the custom plotter feature.

Instead of specifying the additional parameters on the command line, we provide them in a properties file in the input JAR file. `native-image` automatically looks for files named `native-image.properties` under `META-INF/native-image/` and processes their contents. With Maven projects, the path convention is `META-INF/native-image/${groupId}/${artifactId}/native-image.properties`. In this example, the file is therefore at `META-INF/native-image/com.oracle.substratevm/netty-plot/native-image.properties`, and contains the following:
```
ImageName = netty-plot
Args = --features=com.oracle.svm.nettyplot.PlotterSingletonFeature \
          -H:ReflectionConfigurationResources=${.}/reflection-config.json \
          --delay-class-initialization-to-runtime=io.netty.handler.codec.http.HttpObjectEncoder \
          -H:+SpawnIsolates
```
The `ImageName` property specifies the name of the resulting executable, while `Args` are treated like additional command-line arguments. The `-H:ReflectionConfigurationResources=${.}/reflection-config.json` parameter refers to `reflection-config.json` in the same subdirectory as `native-image.properties`. This configuration file specifies classes which must be available via Java reflection at runtime.
```json
[
  {
    "name": "io.netty.channel.socket.nio.NioServerSocketChannel",
    "methods": [
      { "name": "<init>", "parameterTypes": [] }
    ]
  }
]
```

