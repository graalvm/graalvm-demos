# Build a Shared Library with GraalVM Native Image

This demo shows how to create a small Java class library, use `native-image` to create a native shared library, and then create a small C application that uses that shared library.

GraalVM makes it easy to use C to call into a native shared library.
There are two primary mechanisms for calling a method (function) embedded in a native shared library: the [Native Image C API](https://www.graalvm.org/latest/reference-manual/native-image/native-code-interoperability/C-API/) and the [JNI Invocation API](https://www.graalvm.org/latest/reference-manual/native-image/native-code-interoperability/JNIInvocationAPI/).

This example demonstrates how to use the **Native Image C API**. We will:

1. Create and compile a Java class library containing at least one entrypoint method.
2. Use the `native-image` tool to create a shared library from the Java class library.
3. Create and compile a C application that calls an entrypoint method in the shared library.

## Preparation

1. Download and install the latest GraalVM JDK with Native Image and LLVM toolchain using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader). With the LLVM toolchain, you can compile C/C++ code to bitcode using `clang` shipped with GraalVM:
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) -c 'native-image,llvm-toolchain'
    ```
    > Note: The `llvm-toolchain` GraalVM component is not available on Microsoft Windows.

2. Download or clone GraalVM demos repository and navigate into the `native-shared-library` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-shared-library
    ```
3. Compile _LibEnvMap.java_ and build a native shared library, as follows:
    ```bash
    $JAVA_HOME/bin/javac LibEnvMap.java
    ```
    ```bash
    $JAVA_HOME/bin/native-image -H:Name=libenvmap --shared 
    ``` 

    It will produce the following artifacts:
    ```
    --------------------------------------------------
    Produced artifacts:
    /demo/libenvmap.dylib (shared_lib)
    /demo/libenvmap.h (header)
    /demo/graal_isolate.h (header)
    /demo/libenvmap_dynamic.h (header)
    /demo/graal_isolate_dynamic.h (header)
    ==================================================
    ```
    If you work with C or C++, use these header files directly. For other languages, such as Java, use the function declarations in the headers to set up your foreign call bindings. 

    In the result of this process the native shared library will have the `main()` method of the given Java class as its **entrypoint** method.

    If your library does not include a `main()` method, use the `-H:Name=` command-line option to specify the library name, as follows:

    ```bash
    native-image --shared -H:Name=<libraryname> <class name>
    ```
    ```bash
    native-image --shared -jar <jarfile> -H:Name=<libraryname>
    ```  

4. Compile the _main.c_ using `clang`.  
    ```bash
    $JAVA_HOME/languages/llvm/native/bin/clang -I ./ -L ./ -l envmap -Wl,-rpath ./ -o main main.c 
    ```
5. And finally, run the C application by passing a string as an argument. For example:
    ```bash
    ./main USER
    ```
    It will correctly print out the name and value of the matching environment variable(s).    
    
### Tips and Tricks
 
The shared library must have at least one **entrypoint** method.
By default, only a method named `main()`, originating from a `public static void main()` method, is identified as an **entrypoint** and callable from a C application.

To export any other Java method:

* Declare the method as static.
* Annotate the method with `@CEntryPoint` (`org.graalvm.nativeimage.c.function.CEntryPoint`).
* Make one of the method's parameters of type `IsolateThread` or `Isolate`, for example, the first parameter (`org.graalvm.nativeimage.IsolateThread`) in the method below. This parameter provides the current thread's execution context for the call.
* Restrict your parameter and return types to non-object types. These are Java primitive types including pointers, from the `org.graalvm.nativeimage.c.type` package.
* Provide a unique name for the method. If you give two exposed methods the same name, the `native-image` builder will fail with the `duplicate symbol` message. If you do not specify the name in the annotation, you must provide the `-H:Name=libraryName` flag at build time.

Below is an example of an **entrypoint** method:
```java
@CEntryPoint(name = "function_name")
static int add(IsolateThread thread, int a, int b) {
    return a + b;
}
```

When the `native-image` tool builds a native shared library, it also generates a C header file.
The header file contains declarations for the [Native Image C API](https://www.graalvm.org/latest/reference-manual/native-image/native-code-interoperability/C-API/) (which enables you to create isolates and attach threads from C code) as well as declarations for each **entrypoint** in the shared library.
The `native-image` tool generates a C header file containing the following C declaration for the example above:
```c
int add(graal_isolatethread_t* thread, int a, int b);
```

A native shared library can have an unlimited number of **entrypoints**, for example to implement callbacks or APIs.

The advantage of using the [Native Image C API](https://www.graalvm.org/latest/reference-manual/native-image/native-code-interoperability/C-API/) is that you can determine what your API will look like. 
The restriction is that your parameter and return types must be non-object types.
If you want to manage Java objects from C, you should consider [JNI Invocation API](https://www.graalvm.org/latest/reference-manual/native-image/native-code-interoperability/JNIInvocationAPI/). 

### Related Documentation

* Reading the [Embedding Truffle Languages](https://nirvdrum.com/2022/05/09/truffle-language-embedding.html) blog post by Kevin Menard where he compares both mechanisms for exposing Java methods.
* [Interoperability with Native Code](https://www.graalvm.org/latest/reference-manual/native-image/native-code-interoperability/)
* [Java Native Interface (JNI) in Native Image](https://www.graalvm.org/latest/reference-manual/native-image/native-code-interoperability/JNIInvocationAPI/)
* [Native Image C API](https://www.graalvm.org/latest/reference-manual/native-image/native-code-interoperability/C-API/)
