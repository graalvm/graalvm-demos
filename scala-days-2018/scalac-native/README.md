# Native Image of the Scala Compiler

This demo demonstrates how to build a native image of the Scala compiler.

First, make sure that environment variables `SCALA_HOME` and `GRAALVM_HOME` point to Scala 2.12.x and GraalVM. Then build the `sbt` project scalac-substitutions:
```
cd scalac-substitutions
sbt package
cd ../
```

To build the native image of the Scala compiler run
```
./scalac-image.sh
```
The produced native image is called `scalac` and has no dependencies on the JDK.

The script `scalac-native` calls the generated compiler and passes all the required paramters (just like `scala` does).

Let's see how do we compare to the JVM on the first run:
```
$ time $SCALA_HOME/bin/scalac HelloWorld.scala

real	0m2.315s
user	0m5.868s
sys	    0m0.248s

& time ./scalac-native HelloWorld.scala

real	0m0.177s
user	0m0.129s
sys	    0m0.034s
```

When compiled with profile-guided optimization (PGO) the native `scalac` is as fast as the one running on the JVM (with the C2 compiler).