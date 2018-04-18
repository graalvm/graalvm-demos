# GraalVM demos: Java Clojure interop ahead-of-time compilation demo.


This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [Leiningen](https://leiningen.org/)
* [GraalVM](http://graalvm.org)

## Preparation

Download or clone the repository and navigate into the `java-clojure-aot` directory:

```
git clone https://github.com/shelajev/graalvm-demos
cd graalvm-demos/java-clojure-aot
```

This is a simple Java / Clojure HelloWorld application.

Before running this example, you need to build the application.

Note that you can use any JDK for building the app, but in the build script we refer to `javac` in the GraalVM to simplify the prerequisites and not depend on you having another JDK installed.

So export the GraalVM home directory as the `$GRAALVM_HOME` and add `$GRAALVM_HOME/bin` to the path. Here's what I have in my `~/.bashrc` on my MacBook, note that your paths are likely to be different depending on the download location.

```
GRAALVM_VERSION=0.32
export GRAALVM_HOME=/Users/${current_user}/repo/graal-releases/graalvm-$GRAALVM_VERSION/Contents/Home
```

Then execute:
```
./build.sh
```

Let's look at the important line of the `build.sh` which creates a native image from our Java application. The `native-image` utility is a part of GraalVM. It is used to compile applications ahead-of-time for faster starup and lower general overhead at runtime.

```
$GRAALVM_HOME/bin/native-image -cp ./target/main.jar -H:Class=main.core
```

It takes a minimum one parameter, the main class of the application with the `-H:Class=...`

After executing the `native-image` command, check the directory, it should have produced an exacutable file `main.core`.

## Running the application

To run the application, you need to execute the fat jar file in the `target` dir. You can run it as a normal Java application using `java`. Or since we have a native image prepared, you can run that directly.

The `run.sh` file, executes it both ways, and times them with the `time` utility.
```
time java -jar ./target/main.jar

time ./main.core
```

On my machine `run.sh` produces approximately the following output:
```
→ ./run.sh
Hello, World!
java -jar ./target/main.jar  6.16s user 0.46s system 272% cpu 2.427 total
Hello, World!
./main.core  0.00s user 0.00s system 68% cpu 0.009 total
```

The performance gain of the native version is largely due to the faster startup.


## License

The sample application in this directory is generated from lein new projectname 

The code from that repository is distributed under the Apache License 2.0.
The code in this directory is also distributed under the Apache License 2.0, see LICENSE.md for more details.
