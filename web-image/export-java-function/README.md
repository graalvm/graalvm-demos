# Web Image: Export Java Method Example

This demo illustrates the use of **Web Image** - an experimental backend for [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/) that compiles a Java application ahead-of-time and produces a WebAssembly (Wasm) module with a JavaScript wrapper.
Then it can be run in browsers, Node.js, or on the [GraalJS-based](https://github.com/oracle/graaljs/tree/master/graal-nodejs) Node runtime.

The key idea is to show how you can currently **call Java methods directly from JavaScript** without relying on the `main()` method.
This demo exposes a simple Java method to the global JavaScript scope using the `@JS` annotation from the [Annotation Interface](https://www.graalvm.org/sdk/javadoc/org/graalvm/webimage/api/JS.html).

> Note: Web Image is an experimental technology and under active development. APIs, tooling, and capabilities may change.

## Prerequisites

* An [Early Access build](https://github.com/graalvm/oracle-graalvm-ea-builds/releases) of Oracle GraalVM 25 (25e1) or later.
* All [prerequisites](https://www.graalvm.org/latest/reference-manual/native-image/#prerequisites) required for Native Image building.
* [Binaryen toolchain](https://github.com/WebAssembly/binaryen) version 119 or later, available on the system path. Web Image uses `wasm-as` from `binaryen` as its assembler.
  * **macOS**: It is recommended to install Binaryen using Homebrew, as the pre-built binaries from GitHub may be quarantined by the operating system:
    ```bash
    brew install binaryen
    ```
  * **Other platforms**: Download a pre-built release for your platform from [GitHub](https://github.com/WebAssembly/binaryen/releases).

## Building the WebAssembly Module

1. Compile the Java source file:
    ```bash
    javac Adder.java
    ```
2. Compile the application to WASM by passing the `--tool:svm-wasm` option (it should be the first argument):
    ```bash
    native-image --tool:svm-wasm -H:-AutoRunVM Adder
    ```
    The `-H:-AutoRunVM` option prevents the JVM from starting `main()` automatically. Calling `GraalVM.run` directly allows you to execute code only after the `main` method finished and `globalThis.adder()` is guaranteed to be available.

    The build produces the following artifacts in the working directory:
    - _adder.js_ - a JavaScript runtime wrapper;
    - _adder.js.wasm_- the compiled WebAssembly module containing Java code and runtime elements (object layout, parts of [Substrate VM](https://github.com/oracle/graal/tree/master/substratevm) adapted for Wasm);
    - _adder.js.wat_ - debug artifacts to understand how Java code and runtime components are lowered to WebAssembly.

3. Run the application in a browser using a simple HTTP server (with Python or Java):
    ```bash
    python3 -m http.server 8000
    ```
    ```bash
    jwebserver -p 8000
    ```

4.  Navigate to [http://localhost:8000](http://localhost:8000) in the browser. Enter some numbers, click **Add** and see the result displayed.

## Review the Sample Application

What actually happens? This is the Java source code:
```java
import java.util.function.BiFunction;
import org.graalvm.webimage.api.JS;
import org.graalvm.webimage.api.JSNumber;

public class Adder {
    public static int add(int a, int b) {
        return a + b;
    }

    @JS(args = {"adder"}, value = "globalThis.adder = adder;")
    private static native void export(BiFunction<JSNumber, JSNumber, JSNumber> adder);

    public static void main(String[] args) {
        export((a, b) -> {
            return JSNumber.of(add(a.asInt(), b.asInt()));
        });
    }
}
```

- `@JS` annotation is part of [GraalVM Web Image API](https://www.graalvm.org/sdk/javadoc/org/graalvm/webimage/api/JS.html). It allows you to bridge Java and JavaScript.
- `args = {"adder"}` tells GraalVM that the BiFunction you pass in Java will be available as a JavaScript variable `adder` (not necessary if the Java source code is compiled with the `-parameters` option).
- `value = "globalThis.adder = adder;"` is a raw JavaScript code executed when the export happens; it sets a variable called `adder` to be globally accessible in browsers.

Further down you see the `export` method:
```java
export((a, b) -> {
    return JSNumber.of(add(a.asInt(), b.asInt()));
});
```

- A lambda passed in the `export` method converts JS numbers (`JSNumber`) to Java integers, calls the `add` method, and converts the result back to `JSNumber`.
When `export` is called, GraalVM runs the `@JS` snippet.
This makes the lambda directly callable from JS as `globalThis.adder(...)`.

The next part is calling from JavaScript in HTML, which happens in this part of _index.html_:
```js
<script>
GraalVM.run([]).then(() => {
    ...
    addButton.addEventListener("click", () => {
        const a = parseInt(document.getElementById("num1").value);
        const b = parseInt(document.getElementById("num2").value);

        // Call the Java add function via WebAssembly
        const result = globalThis.adder(a, b);

        output.innerText = `Result: ${result}`;
    });
    ...
});
</script>
```

- `GraalVM.run([], {})` initializes the Wasm module and the Java runtime inside the browser.
- `globalThis.adder(a, b)` calls the `add` function you exported via WebAssembly, after the runtime is ready.

### Conclusion

The focus of this demo is to demonstrate direct interaction between Java and JavaScript in the browser via WebAssembly.
Note that the [GraalVM Web Image API](https://www.graalvm.org/sdk/javadoc/org/graalvm/webimage/api/JS.html) is still under active development, there will be better ways to export Java methods to JavaScript.
