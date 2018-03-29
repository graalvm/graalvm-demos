# GraalVM demos: Native images for faster startup


This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [GraalVM](http://graalvm.org)

## Preparation

Download or clone the repository and navigate into the `native-list-dir` directory:

```
git clone https://github.com/shelajev/graalvm-demos
cd graalvm-demos/native-list-dir
```

There are two Java classes we'll the `ListDir.java` for the purposes of this demo.
First we need to build this class. You can manually execute `javac ListDir.java`, but there's also a `build.sh` script included for your convenience.

Note that you can use any JDK for compiling the Java classes, but in the build script we refer to `javac` in the GraalVM to simplify the prerequisites and not depend on you having another JDK installed.

So export the GraalVM home directory as the `$GRAALVM_HOME` and add `$GRAALVM_HOME/bin` to the path. Here's what I have in my `~/.bashrc` on my MacBook, note that your paths are likely to be different depending on the download location.

```
GRAALVM_VERSION=0.32
export GRAALVM_HOME=/Users/${current_user}/repo/graal-releases/graalvm-$GRAALVM_VERSION/Contents/Home
```

Then execute:
```
./build.sh
```

The important line of the `build.sh` creates a native image from our Java class. Let's look at it in more detail:

```
$GRAALVM_HOME/bin/native-image ListDir
```

The `native-image` utility is a part of GraalVM. It is used to compile applications ahead-of-time for faster starup and lower general overhead at runtime.

After executing the `native-image` command, check the directory, it should have produced an exacutable file `listdir`.


## Running the application

To run the application, you need to execute the `ListDir` class. You can run it as a normal Java application using `java`. Or since we have a native image prepared, you can run that directly.

The `run.sh` file, executes both, and times them with the `time` utility.
```
time java ListDir $1
time ./listDir $1
```

To make it a little bit more interesting pass it a directory that actually contains some file: `./run.sh ..` (`..` - is the parent of the current directory, the one containing all the demos in this repository).

On my machine it produces approximately the following output:
```
→ ./run.sh ../..
+ java ListDir ../..
Walking path: ../..
Total: 26233 files, total size = 556731978 bytes

real	0m1.715s
user	0m3.613s
sys	0m1.133s
+ ./listDir ../..
Walking path: ../..
Total: 26233 files, total size = 556731978 bytes

real	0m0.883s
user	0m0.203s
sys	0m0.657s
```

The performance gain of the native version is largely due to the faster startup.

## ExtListDir class

You can also experiment with a more sophisticated `ExtListDir` example, which uses Java / JavaScript polyglot capabilities and

To compile that class you need to add `graal-sdk.jar` on the classpath:

```
$GRAALVM_HOME/bin/javac -cp $GRAALVM_HOME/jre/lib/boot/graal-sdk.jar ExtListDir.java
```

Building the native image command is similar to the one above, but since we want to use JavaScript, we need to inform the `native-image` utility about it passing the `--js` option.
Note that it takes a bit more time because it needs to include the JavaScript support.

```
$GRAALVM_HOME/bin/native-image --js ExtListDir
```

Executing it is the same as in the previous example:

```
time java ExtListDir $1
time ./extlistdir $1
```
