# Java Streams API Benchmark on GraalVM

This demo shows how GraalVM efficiently removes abstractions from high-level programs.
The `Streams.java` file contains a simple query implemented with the Java Streams API:
```java
Arrays.stream(persons)
   .filter(p -> p.getEmployment() == Employment.EMPLOYED)
   .filter(p -> p.getSalary() > 100_000)
   .mapToInt(Person::getAge)
   .filter(age -> age > 40)
   .average()
   .getAsDouble();
```
## Preparation

1. Run this stream with any `java` you have on your system:
    ```bash
    javac Streams.java
    ```
    ```bash
    java Streams 100000 200
    ```
    ```
    Iteration 20 finished in 114 milliseconds with checksum e6e0b70aee921601
    TOTAL time: 2330
    ```

2. Download and install the latest GraalVM JDK using [SDKMAN!](https://sdkman.io/).
    ```bash
    sdk install java 21.0.1-graal
    ```

3. Now compile and run the same application on GraalVM:
    ```bash
    $JAVA_HOME/bin/javac Streams.java
    ```
    ```bash
    $JAVA_HOME/bin/java Streams 100000 200
    ```
    ```
    Iteration 20 finished in 21 milliseconds with checksum e6e0b70aee921601
    TOTAL time: 623
    ```

We can see over 4x speedup on this simple program.

## Profile-Guided Optimizations with Native Image

This demo shows how to use profile-guided optimizations (PGO) with the `native-image` builder.

> Note: Profile-guided optimizations is a GraalVM Enterprise feature.

You will use the same `Streams.java` program that contains a simple query implemented with the Java Streams API:
```java
Arrays.stream(persons)
   .filter(p -> p.getEmployment() == Employment.EMPLOYED)
   .filter(p -> p.getSalary() > 100_000)
   .mapToInt(Person::getAge)
   .filter(age -> age > 40)
   .average()
   .getAsDouble();
```

1. First, build the native image without profile-guided optimizations:
    ```bash
    $JAVA_HOME/bin/javac Streams.java
    ```
    ```bash
    $JAVA_HOME/bin/native-image Streams
    ```
    ```
    ./streams 100000 200
    ```
    ```
    Iteration 20 finished in 127 milliseconds with checksum e6e0b70aee921601
    TOTAL time: 2589
    ```
    This version of the program runs about 2x slower than the one on the regular JDK.

2. To enable PGO you need to build an instrumented image:
    ```bash
    $JAVA_HOME/bin/native-image --pgo-instrument Streams
    ```
3. Run this instrumented image to collect profiles:
    ```bash
    ./streams 10000 200
    ```
    ```
    Iteration 20 finished in 62 milliseconds with checksum e1d39d010d08cf01
    TOTAL time: 1279
    ```
    Profiles collected from this run are now stored in the _default.iprof_ file.
    
    Note that we run the profiling with a much smaller data size.
    
4. Now you can use the collected profiles to build an optimized native image:
    ```bash
    $JAVA_HOME/bin/native-image --pgo Streams
    ```

5. Then run it:
    ```bash
    ./streams  100000 200
    ```
    ```
    Iteration 20 finished in 24 milliseconds with checksum e6e0b70aee921601
    TOTAL time: 516
    ```

You should get the performance comparable to the Java version of the program.

## Related Documentation

[Optimize a Native Executable with PGO](https://www.graalvm.org/latest/reference-manual/native-image/guides/optimize-native-executable-with-pgo/)