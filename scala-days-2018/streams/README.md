# Abstraction Without Regret When Running on GraalVM

This demo shows how GraalVM efficiently removes abstractions from high-level programs. The `TestStream.java` file contains a simple query implemented with the Java Streams API:
```
Arrays.stream(persons)
   .filter(p -> p.getEmployment() == Employment.EMPLOYED)
   .filter(p -> p.getSalary() > 100_000)
   .mapToInt(Person::getAge)
   .filter(age -> age > 40)
   .average()
   .getAsDouble();
```

Let us run this stream with regular `java`:
```
$ javac TestStream.java
$ java TestStream
...
Iteration 20 finished in 219 milliseconds.
TOTAL time: 4717
```

Now when we run the same with GraalVM:
```
$ $GRAALVM_HOME/bin/java TestStream
...
Iteration 20 finished in 48 milliseconds.
TOTAL time: 2406
```

We can see over 4x speeup on this simple program.

# Profile-Guided Optimizations with `native-image`

This demo shows how to use profile-guided optimizations (PGO) in `native-image`. We will use the `TestStream.java` program that contains a simple query implemented with the Java Streams API
```
Arrays.stream(persons)
   .filter(p -> p.getEmployment() == Employment.EMPLOYED)
   .filter(p -> p.getSalary() > 100_000)
   .mapToInt(Person::getAge)
   .filter(age -> age > 40)
   .average()
   .getAsDouble();
```

Let us first build the native image without profile-guided optimizations:
```
$ javac TestStream.java
$ $GRAALVM_HOME/bin/native-image TestStream
$ ./teststream
...
Iteration 20 finished in 452 milliseconds.
TOTAL time: 9085
```
This version of the program runs about 2x slower than the one on the regular JDK.

To enable PGO we need to build an instrumented image and run it to collect profiles:
```
$ $GRAALVM_HOME/bin/native-image --pgo-instrument TestStream
$ ./teststream
```
Profiles collected from this run are now stored in the `default.iprof` file.

Now we can use these profiles to make an optimized image:
```
$ $GRAALVM_HOME/bin/native-image --pgo TestStream
```
When we run it
```
$ ./teststream
...
Iteration 20 finished in 254 milliseconds.
TOTAL time: 5141
```

We get performance comparable to the Java version of the program.
