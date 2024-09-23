# GraalWasm Quick Start

A minimal Java application that embeds a WebAssembly module with GraalWasm.

## Preparation

Install GraalVM for JDK 23 and set the value of `JAVA_HOME` accordingly.
We recommend using [SDKMAN!](https://sdkman.io/). (For other download options, see [GraalVM Downloads](https://www.graalvm.org/downloads/).)

```bash
sdk install java 23-graal
```

## Run the Application Using Maven

To build and test the demo, run:

```bash
./mvnw test
```

To execute the main method, run:

```bash
./mvnw exec:java
```

## Run the Application Using Gradle

To build and test the demo, run:

```bash
./gradlew test
```

To execute the main method, run:

```bash
./gradlew run
```

## Implementation Details

The WebAssembly is stored in the resource file `add-two.wasm`.
You can examine its textual representation in the resource file `add-two.wat`.
If you want to experiment with and tweak the WebAssembly module, you will need to rebuild the `add-two.wasm` file from the `add-two.wat` file.
For that, you can use the `wat2wasm` tool from the [wabt toolkit](https://github.com/WebAssembly/wabt).
You can also use [this web app](https://webassembly.github.io/wabt/demo/wat2wasm/) to run `wat2wasm` in your browser instead of installing `wabt`.
