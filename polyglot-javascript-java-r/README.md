# GraalVM demos: Polyglot JavaScript, Java, R application

This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [GraalVM](http://graalvm.org)

## Preparation

1. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$GRAALVM_HOME` and add `$GRAALVM_HOME/bin` to the `PATH` environment variable.
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

2. Download or clone the repository and navigate into the `polyglot-javascript-java-r` directory:

```
git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos/polyglot-javascript-java-r
```

3. Build the benchmark. You can manually execute `npm install`, but there's also a `build.sh` script included for your convenience:
```
./build.sh
```

Now you are all set to run the polyglot JavaScript, Java, R application.

## Running the application
To run the application, you need to execute the `server.js` file. You can run it with the following command (or run the `run.sh` script):

```
$GRAALVM_HOME/bin/node --jvm --polyglot server.js
```

If you would like to run the benchmark on a different instance of Node, you can run it with whatever `node` you have. However, usually the polyglot capability won't be supported and the app won't run successfully.

Open [localhost:3000](localhost:3000) and enjoy the output of the polyglot app. Play with the source code and restart the application to see what else can you do with the mix of JavaScript, Java, and R.

## Debugging Polyglot Applications

GraalVM supports debugging polyglot applications too, add the `--inspect` parameter to the command line, open the url the application prints at the startup in Chrome browser and you can debug, set breakpoints, evaluate expressions in this app in the JavaScript and R code alike.

## A Note About the Application

This is a sample application that for brevity contains reasonably large snippets of code inside the strings. This is not the best approach for structuring polyglot apps, but the easiest to show in a single file.
