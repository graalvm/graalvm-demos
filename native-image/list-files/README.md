# Create a Native Executable and Apply Profile-Guided Optimization

This demo shows how to create a native executable with GraalVM Native Image and apply Profile-Guided Optimization (PGO) for more performance gains.
It uses the Java application that counts files and their sizes in a specified directory.

## Run the Demo

1. Compile the application:
    ```bash
    javac ListDir.java
    ```

2. Generate a native executable from the class file: 
    ```bash
    native-image ListDir
    ```
    The executable file _listdir_ is created in the directory.

3. Run the application by passing it a directory that actually contains some files, for example `..` (to count files in the parent of the current directory, containing all the demos in this repository). Check the time spent with the `time` utility.
    - On HotSpot:
        ```bash
        time java ListDir ..
        ```
    - From a native executable:
        ```bash
        time ./listdir ..
        ```

## Enable PGO

For more performance gains, you can can apply [Profile-Guided Optimization (PGO)](https://www.graalvm.org/latest/reference-manual/native-image/optimizations-and-performance/PGO/). (Not available with GraalVM Community Edition.)
With PGO you can collect the profiling data, and then feed it to the `native-image` tool, which will use this information to further optimize the performance of the resulting executable.

1. Build an instrumented image from the _ListDir_ class and run it to collect profiles, specifying a different name for the native executable, for example _listdir-instrumented_:

    ```shell
    native-image --pgo-instrument ListDir -o listdir-instrumented
    ```
    ```bash
    ./listdir-instrumented ..
    ```
    Profiles collected from this run are now stored in the `default.iprof` file. 

2. Use the profiles to build an optimized native executable, giving it a different name than in the previous runs:
    ```shell
    native-image --pgo ListDir -o listdir-optimized
    ```

3. Run that optimized executable:
    ```shell
    time ./listdir-optimized ..
    ```

Find more examples at the website:
- [Optimize a Native Executable with Profile-Guided Optimizations](https://www.graalvm.org/latest/reference-manual/native-image/guides/optimize-native-executable-with-pgo/).
- [Basic Usage of Profile-Guided Optimization](https://www.graalvm.org/latest/reference-manual/native-image/optimizations-and-performance/PGO/basic-usage/)