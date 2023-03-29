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

HOW TO GET RSS?

Test the application using a browser or `curl`, as follows:

```bash
curl http://localhost:8080/redrum
```

You should see output similar to the following:
```
Time taken to load words: 589
murder
```

(The time taken is measure in milliseconds.)

## 2. Build and Run a Native Executable 

1. Build a native executable using the following command. The java agent collects metadata and generates the configuration files in the _target/classes/META-INF/native-image_ directory.

    ```bash
    ./mvnw package -Dpackaging=native-image 
    ```

    When the command completes a native executable, `Anagram_Server`, is created in the _target_ directory of the project and ready for use.
The file size is 74557760 bytes.

2. Run the native executable in the background, as follows:

    ```bash
    ./target/Anagram_Server &
    ```

    HOW TO GET RSS?

3. Test the application using a browser or `curl`, as follows:

    ```bash
    curl http://localhost:8080/redrum
    ```

    ```
    Time taken to load words: 678
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
    Time taken to load words: 495
    ```

    When the command completes a native executable, `Anagram_Server`, is again created in the _target_ directory of the project and ready for use.
    The file size is 107702592 bytes: over 40% bigger than the original native executable, because the heap now contains the words that were loaded.

2. Run the native executable in the background, as follows:

    ```bash
    ./target/Anagram_Server &
    ```

    HOW TO GET RSS?


3. Test the application using a browser or `curl`, as follows:

    ```bash
    curl http://localhost:8080/redrum
    ```

    ```
    murder
    ```

    Note that the application responded instantly: the words had already been loaded at build time.
