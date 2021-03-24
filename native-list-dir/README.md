# GraalVM Demos: Native images for faster startup

This repository contains the code for a demo application for [GraalVM](http://graalvm.org).

## Prerequisites
* [GraalVM](http://graalvm.org)
* [Native Image](https://www.graalvm.org/docs/reference-manual/native-image/)

## Preparation

1. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$GRAALVM_HOME`, and add `$GRAALVM_HOME/bin` to the `PATH` environment variable.

On Linux:
```
export GRAALVM_HOME=/home/${current_user}/path/to/graalvm
export PATH=$GRAALVM_HOME/bin:$PATH
```
On macOS:
```
export GRAALVM_HOME=/Users/${current_user}/path/to/graalvm/Contents/Home
export PATH=$GRAALVM_HOME/bin:$PATH
```

2. Install [Native Image](https://www.graalvm.org/docs/reference-manual/native-image/#install-native-image):
```
gu install native-image
```

3. Download or clone the repository and navigate into the `native-list-dir` directory:

```
git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos/native-list-dir
```

There are two Java classes. Start by building `ListDir.java` for the purposes of this demo.
You can manually execute `javac ListDir.java`, but there is also a `build.sh` script included for your convenience.

Note that you can use any JDK for compiling the Java classes, but in the build script GraalVM's `javac` is used to simplify the prerequisites and not depend on you having another JDK installed.

4. Then execute:
```
./build.sh
```

The important line of the `build.sh` creates a native image from our Java class. Have a look at it:

```
$GRAALVM_HOME/bin/native-image ListDir
```

The `native-image` compiles the application ahead-of-time for faster startup and lower general overhead at runtime.
After executing the `native-image` command, check the directory. It should have produced an executable file `listdir`.

## Running the application

To run the application, you need to execute the `ListDir` class. You can run it as a normal Java application using `java`. Or, since we have a native image prepared, you can run that directly. The `run.sh` file, executes both, and times them with the `time` utility.
```
time java ListDir $1
```

To make it a little bit more interesting, pass it a directory that actually contains some file: `./run.sh ..` (`..` - is the parent of the current directory, the one containing all the demos in this repository).

Approximately the following output should be produced (the files count and their sizes will vary of course):
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

You can also experiment with a more sophisticated `ExtListDir` example, which uses Java/JavaScript polyglot capabilities.
Compile the example:

```
$GRAALVM_HOME/bin/javac ExtListDir.java
```

Building the native image command is similar to the one above, but since you want to use JavaScript, inform the `native-image` builder about it passing the `--language:js` option.
Note that it will take more time because it needs to include the JavaScript support.

```
$GRAALVM_HOME/bin/native-image --language:js ExtListDir
```

The execution is the same as in the previous example:

```
time java ExtListDir $1
time ./extlistdir $1
```
