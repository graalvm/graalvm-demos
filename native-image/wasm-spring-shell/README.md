# Compile Spring Shell into a Wasm Module

This demo illustrates how to use GraalVM Web Image to compile a Spring Shell application into a Wasm module that can then run on the command-line or in the browser. [Check out the live demo here](https://graalvm.github.io/graalvm-demos/native-image/wasm-spring-shell/).

## Prerequisites

This demo requires:

1. This [Early Access Build](https://github.com/graalvm/oracle-graalvm-ea-builds/releases/tag/jdk-25e1-25.0.0-ea.01) of Oracle GraalVM 25.1 or later.
2. The [Binaryen toolchain](https://github.com/WebAssembly/binaryen) in version 119 or later and on the system path.
    For example, using Homebrew: `brew install binaryen`

## Run Spring Shell on Node

1. Build the Wasm module with the `native` profile:
    ```bash
    $ ./mvnw -Pnative package
    ```
    The demo uses the [Native Build Tools](https://graalvm.github.io/native-build-tools/latest/index.html) for building native images with GraalVM and Maven.
    This command generates a Wasm file and a corresponding JavaScript binding in the `target` directory.

2. Run the Wasm module on Node:
    ```bash
    node target/wasm-spring-shell help
    node target/wasm-spring-shell hello Jane
    ```
    This requires Node.js 22 or later.
