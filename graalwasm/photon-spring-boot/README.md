# Photon with GraalWasm and Spring Boot Demo

This demo illustrates how GraalWasm can be used to embed [Photon](https://silvia-odwyer.github.io/photon/), a WebAssembly image processing library written in Rust, in a Spring Boot application.
The demo also uses GraalJS to access the Photon module through the WebAssembly JavaScript API.

## Preparation

Install GraalVM for JDK 23 and set the value of `JAVA_HOME` accordingly.
We recommend using [SDKMAN!](https://sdkman.io/). (For other download options, see [GraalVM Downloads](https://www.graalvm.org/downloads/).)

```bash
sdk install java 23-graal
```

## Run the Application

To start the demo, simply run:

```bash
./mvnw package spring-boot:run
```

When the demo runs, open the following URLs in a browser:

- http://localhost:8080/photo/default
- http://localhost:8080/photo/grayscale
- http://localhost:8080/photo/colorize
- http://localhost:8080/photo/fliph
- http://localhost:8080/photo/flipv

To compile the application with GraalVM Native Image, run:

```bash
./mvnw -Pnative native:compile
./target/demo
```

To use [Profile-Guided Optimization](https://www.graalvm.org/latest/reference-manual/native-image/optimizations-and-performance/PGO/), run the following commands:

```bash
# Compile and run instrumented image
./mvnw -Pnative,pgo-instrument native:compile
./target/demo-g1-pgo-instrument

# Produce some load, for example using https://github.com/rakyll/hey
hey -c 8 -z 2m http://localhost:8080/photo/flipv
# Quitting the demo-g1-pgo-instrument process will generate a profile file (default.iprof)

# Compile and run optimized image
./mvnw -Pnative,pgo native:compile
./target/demo-g1-pgo
```

## Implementation Details

The [`DemoController`](src/main/java/com/example/demo/DemoController.java) uses a [`PhotonService`](src/main/java/com/example/demo/PhotonService.java) to implement the `/photo/{effectName}` endpoint.
This service accesses `Photon` objects that are pooled in a [`PhotonPool`](src/main/java/com/example/demo/PhotonPool.java) to check whether an effect for a given effectName exists before applying the effect to a sample image.

`PhotonPool` creates a `Photon` object for each available processor, which in turn holds a reference to the corresponding `photonModule` and an `imageContent` object.
Both of these objects are from JavaScript and backed by the same [`Context`](https://www.graalvm.org/sdk/javadoc/org/graalvm/polyglot/Context.html).
Note that the `Context` objects share the same [`Engine`](https://www.graalvm.org/sdk/javadoc/org/graalvm/polyglot/Engine.html), to improve warmup and memory footprint.

Also note that the Photon JavaScript and WebAssembly modules as well as the sample images are downloaded when the demo is built with the `wagon-maven-plugin` Maven plugin (see _pom.xml_).

The [`DemoApplicationTests`](src/test/java/com/example/demo/DemoApplicationTests.java) tests that applying the same effect on two copies of the same image yields the same result, regardless of the effect used.
Run it with `./mvnw test`.
