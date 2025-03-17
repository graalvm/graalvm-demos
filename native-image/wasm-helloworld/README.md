# Build a Wasm Module with GraalVM

This demo illustrates how to compile a simple HelloWasm Java example into a Wasm Module.
  
1. Download the latest early access build of Oracle GraalVM for JDK 25, from here or via SDKMAN! (e.g., `sdk install java 25.ea.8-graal`).

2. Download the latest release of [Binaryen](https://github.com/WebAssembly/binaryen) and put its bin directory on the system path.

3. Use javac to compile your Java sources to JVM bytecode:
    ```bash
    javac HelloWasm.java
    ```

4. Compile JVM bytecode to Wasm using the `--tool:svm-wasm` option:
    ```bash
    native-image --tool:svm-wasm HelloWasm.java
    ```

5. Run the `hellowasm` Wasm module, for example on Node 22 using the automatically generated `hellowasm.js` JavaScript wrapper:
    ```bash
    node hellowasm.js
    ```
