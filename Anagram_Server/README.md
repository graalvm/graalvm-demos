# Anagram server

A simple HTTP server to suggest anagrams in response to a request.

Example usage: _[http://localhost:8080/redrum](http://localhost:8080/redrum)_

(Words are taken from from [https://github.com/dwyl/english-words](https://github.com/dwyl/english-words).)


## 1. Build and Run the Java Application

Build and run the project on the JVM. Open a terminal window and, from the root application directory, run the following command.
(Use `&` to put the process into the background.)

```bash
./mvnw mn:run &
```

You should see output similar to the following, indicating the time taken to startup the application.
```
12:24:19.507 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 1223ms. Server Running: http://localhost:8080
```

>HOW TO GET RSS?

Test the application using a browser or `curl`, as follows:

```bash
curl http://localhost:8080/redrum
```

You should see output similar to the following:
```
12:24:41.352 [default-nioEventLoopGroup-1-2] INFO  com.example.AnagramSolver - Time taken to load words: 798ms
murder
```

## 2. Build and Run a Native Executable 

1. Build a native executable using the following command. The Java agent collects metadata and generates the configuration files in the _target/classes/META-INF/native-image_ directory.

    ```bash
    ./mvnw package -Dpackaging=native-image 
    ```

    When the command completes a native executable, `Anagram_Server`, is created in the _target_ directory of the project and ready for use.
The size of the file is 74557792 bytes.

2. Run the native executable in the background, as follows:

    ```bash
    ./target/Anagram_Server &
    ```

    You should see output similar to the following, indicating the time taken to startup the application.
    ```
    12:49:25.025 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 66ms. Server Running: http://localhost:8080
    ```

    > HOW TO GET RSS?

3. Test the application using a browser or `curl`, as follows:

    ```bash
    curl http://localhost:8080/redrum
    ```

    ```
    12:50:01.320 [default-nioEventLoopGroup-1-2] INFO  com.example.AnagramSolver - Time taken to load words: 501ms
    murder
    ```

## 3. Use Static Initializer to improve Initial Response Time

1. Build a native executable using the following command. Again the java agent collects metadata and generates the configuration files in the _target/classes/META-INF/native-image_ directory.

    ```bash
    ./mvnw -Pstatic package
    ```

    This relies on the "static" profile in the _pom.xml_ file, which includes the following element:

    ```xml
    <buildArgs>
      <buildArg>--initialize-at-build-time=com.example.AnagramSolver</buildArg>
    </buildArgs>
    ```

    In the output you should see something similar to 

    ```
    ========================================================================================================================
    GraalVM Native Image: Generating 'Anagram_Server' (executable)...
    ========================================================================================================================
    ...
    12:52:37.311 [main] INFO  com.example.AnagramSolver - Time taken to load words: 508ms
    ```

    When the command completes a native executable, `Anagram_Server`, is again created in the _target_ directory of the project and ready for use.
    The file size is 107702592 bytes: over 40% bigger than the original native executable, because the heap now contains the words that were loaded.

2. Run the native executable in the background, as follows:

    ```bash
    ./target/Anagram_Server &
    ```

    You should see output similar to the following, indicating the time taken to startup the application.
    ```
    13:03:58.931 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 66ms. Server Running: http://localhost:8080
    ``` 

    > HOW TO GET RSS?

3. Test the application using a browser or `curl`, as follows:

    ```bash
    curl http://localhost:8080/redrum
    ```

    ```
    murder
    ```

    Note that the application responded instantly, with no log message: the words had already been loaded at build time.
    