# Web Image: Export Java Method with JavaScript Objects

Unlike the [Export Java Method Example](../export-java-function/) which exchanges primitive values like numbers, this demo illustrates how you can work with **objects**: JavaScript sends a request object, Java processes it, and returns a structured response object, via WebAssembly.
Object fields can be read directly with typed `get` calls.

This tiny in-browser service is compiled to WebAssembly using **Web Image** - an experimental backend for [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/) that can compile a JVM application ahead-of-time and produce a Wasm module with a JavaScript wrapper.
You can run this small backend service in a browser or on Node.js.

> Note: [Web Image](https://www.graalvm.org/latest/reference-manual/web-image/) is an experimental technology and under active development. APIs, tooling, and capabilities may change.

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
    javac PricingService.java
    ```

2. Compile the application to Wasm by passing the `--tool:svm-wasm` option (it should be the first argument):
    ```bash
    native-image --tool:svm-wasm -H:-AutoRunVM PricingService
    ```
    The `-H:-AutoRunVM` option prevents the VM from starting `main()` automatically. Calling `GraalVM.run` from JavaScript ensures that the exported function is available after the runtime initializes.

    The build produces the following artifacts:

    * _pricingservice.js_ — JavaScript runtime wrapper
    * _pricingservice.js.wasm_ — the compiled WebAssembly module
    * _pricingservice.js.wat_ — debug artifact showing the generated WebAssembly text format

3. Run the application in a browser using a simple HTTP server:
    ```bash
    python3 -m http.server 8000
    ```
    ```bash
    jwebserver -p 8000
    ```
    Navigate to [http://localhost:8000](http://localhost:8000), and click the button in the demo page to send a request object to Java and display the processed result.

## Review the Sample Application

This example exports a single Java method that calculates a price based on user input.
JavaScript sends a request object, Java handles it in `handleRequest(...)`, and returns a response object.

```java
import java.util.function.Function;
import org.graalvm.webimage.api.JS;
import org.graalvm.webimage.api.JSObject;
import org.graalvm.webimage.api.JSNumber;

public class PricingService {

    @JS(args = {"handler"}, value = "globalThis.pricingService = handler;")
    private static native void export(Function<JSObject, JSObject> handler);

    public static void main(String[] args) {
        export(PricingService::handleRequest);
    }

    private static JSObject handleRequest(JSObject request) {
        String operation = request.get("operation", String.class);
        int price = request.get("price", Integer.class);

        JSObject user = request.get("user", JSObject.class);
        boolean premium = user.get("premium", Boolean.class);

        int discount = 0;

        if ("discount".equals(operation)) {
            discount = premium ? 20 : 10;
        }

        int finalPrice = price - (price * discount / 100);

        JSObject response = JSObject.create();
        response.set("finalPrice", JSNumber.of(finalPrice));
        response.set("discountApplied", JSNumber.of(price - finalPrice));
        response.set("premium", premium);
        return response;
    }
}
```

### Annotating the Function

* The `@JS` annotation exposes a JavaScript code snippet to Java. It is part of [GraalVM Web Image API](https://www.graalvm.org/sdk/javadoc/org/graalvm/webimage/api/JS.html).
* `args = {"handler"}` defines the variable name available in JavaScript.
* The JavaScript snippet assigns the function to `globalThis.pricingService`, and makes it callable from JavaScript once the runtime is ready.

### Exporting a Method

```java
export(PricingService::handleRequest);
```
* JavaScript needs a callable value. With Web Image API, that callable can be an anonymous lambda, a method reference, or a named implementation class.
* In this demo, the callable is a Java method exposed via the method reference `PricingService::handleRequest`.
* JavaScript sends an object, Java processes it, and returns another object.

### Reading Properties from a Request Object

Next Java reads properties from a JavaScript `request` object.
With typed `get` operations, Java values can be read directly.
```java
String operation = request.get("operation", String.class);
int price = request.get("price", Integer.class);

JSObject user = request.get("user", JSObject.class);
boolean premium = user.get("premium", Boolean.class);
```
Notice also that nested objects can also be accessed.

### Creating a Response Object

After reading the properties, it creates a `response` object in Java.
This object becomes a normal JavaScript object when returned.
```java
JSObject response = JSObject.create();
response.set("finalPrice", JSNumber.of(finalPrice));
```

### Calling the Java Method from JavaScript

In the HTML file you see:
```js
<script>
GraalVM.run([]).then(() => {
    const button = document.getElementById("run");
    const output = document.getElementById("output");

    button.addEventListener("click", () => {
        const priceInput = parseInt(document.getElementById("price").value, 10);
        const premiumInput = document.getElementById("premium").checked;

        const request = {
            operation: "discount",
            price: Number.isNaN(priceInput) ? 0 : priceInput,
            user: {
                premium: premiumInput,
            },
        };

        const result = globalThis.pricingService(request);
        output.innerText = JSON.stringify(result, null, 2);
    });
});
</script>
```

* `GraalVM.run([])` initializes the WebAssembly runtime. The wrapper should be loaded in HTML before the `GraalVM.run` call.
* After initialization, `globalThis.pricingService(...)` becomes available.
* The Java code runs inside WebAssembly and returns the result.

## Related Documentation

* [Get Started with GraalVM Web Image](https://www.graalvm.org/latest/reference-manual/web-image/)
* [Web Image API](https://www.graalvm.org/sdk/javadoc/org/graalvm/webimage/api/package-summary.html)
