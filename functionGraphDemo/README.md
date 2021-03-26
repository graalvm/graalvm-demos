# GraalVM Demos: FunctionGraph Demo - Polyglot JavaScript, Java, R application

This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [GraalVM](http://graalvm.org)

## Preparation
1. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$GRAALVM_HOME`. and add `$GRAALVM_HOME/bin` to the `PATH` environment variable.
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
2. Download or clone the repository and navigate into the `functionGraphDemo` directory:
```
git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos/functionGraphDemo
```
3. Build the application by running `npm install`.

Now you are all set to run the polyglot JavaScript, Java, R application.

## Running the application
To run the application, you need to execute the `server.js` file.
You can run it with the following command:

```
$GRAALVM_HOME/bin/node --jvm --polyglot server.js
```

If you would like to run the benchmark on a different instance of Node, you can
run it with whatever `node` you have. However, usually the polyglot capability
will not be supported and the app will not run successfully.

Open [localhost:8088](http://localhost:8088/) and enjoy the output. Play with the source
code and restart the application to see what else can you do with the mix of
JavaScript, Java, and R.

## Debugging polyglot applications
GraalVM supports [debugging polyglot applications](https://www.graalvm.org/docs/tools/chrome-debugger) too, add the
`--inspect` parameter to the command line, open the URL the application prints
at the startup in Chrome browser. You can debug, set breakpoints, evaluate
expressions in this app in the JavaScript and R code alike.
