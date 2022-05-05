# Abstraction Without Regret When Running on GraalVM

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

1. Run this stream with any `java` you have on your system:
  ```bash
  javac Streams.java
  java Streams 100000 200
  ...
  Iteration 20 finished in 219 milliseconds with checksum e6e0b70aee921601
  TOTAL time: 4717
  ```

2. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$JAVA_HOME` and add `$JAVA_HOME/bin` to the `PATH` environment variable.

  On Linux:
  ```bash
  export JAVA_HOME=/home/${current_user}/path/to/graalvm
  export PATH=$JAVA_HOME/bin:$PATH
  ```
  On macOS:
  ```bash
  export JAVA_HOME=/Users/${current_user}/path/to/graalvm/Contents/Home
  export PATH=$JAVA_HOME/bin:$PATH
  ```
  On Windows:
  ```bash
  setx /M JAVA_HOME "C:\Progra~1\Java\<graalvm>"
  setx /M PATH "C:\Progra~1\Java\<graalvm>\bin;%PATH%"
  ```

3. Now compile and run the same application on GraalVM:
  ```bash
  javac Streams.java
  $JAVA_HOME/bin/java Streams
  ...
  Iteration 20 finished in 36 milliseconds with checksum e6e0b70aee921601
  TOTAL time: 1271
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
  javac Streams.java
  $JAVA_HOME/bin/native-image Streams
  ./streams 100000 200
  ...
  Iteration 20 finished in 452 milliseconds with checksum e6e0b70aee921601
  TOTAL time: 4747
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
  Profiles collected from this run are now stored in the `default.iprof` file.
  Note that we run the profiling with a much smaller data size.

4. Now you can use the collected profiles to build an optimized native image:
  ```bash
  $JAVA_HOME/bin/native-image --pgo Streams
  ```

5. Then run it:
  ```bash
  ./streams  100000 200
  ...
  Iteration 20 finished in 1 milliseconds with checksum a2708129d726fc01
  TOTAL time: 28
  ```

You should get the performance comparable to the Java version of the program.
