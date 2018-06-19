# Abstraction Without Regret When Running on GraalVM

This demo shows how GraalVM efficiently removes abstractions from high-level programs. The `TestStream.java` file contains a simple query implemented with the Java Streams API:
```
Arrays.stream(persons)
   .filter(p -> p.getGender() == Sex.MALE)
   .filter(p -> p.getHeight() > 100)
   .mapToInt(Person::getAge)
   .filter(age -> age > 10)
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