# GraalVM demos: Another Polyglot JavaScript, Java, R application


This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [GraalVM](http://graalvm.org)

## Preparation

Export the GraalVM home directory as the `$GRAALVM_HOME` and add `$GRAALVM_HOME/bin` to the path.

Download or clone the repository and navigate into the `functionGraphDemo` directory:

```
git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos/functionGraphDemo
```

Build the application by running `npm install`.

Now you're all set to run the polyglot JavaScript, Java, R application.


## Running the application

To run the application, you need to execute the `server.js` file.
You can run it with the following command:

```
$GRAALVM_HOME/bin/node --jvm --polyglot server.js
```

If you'd like to run the benchmark on a different instance of Node, you can run it with whatever `node` you have. However, usually the polyglot capability won't be supported and the app won't run successfully.

Open [localhost:8088](localhost:8088) and enjoy the output of the polyglot app. Play with the source code and restart the application to see what else can you do with the mix of JavaScript, Java, and R.

## Debugging polyglot applications

GraalVM supports debugging polyglot applications too, add the `--inspect` parameter to the command line, open the url the application prints at the startup in Chrome browser and you can debug, set breakpoints, evaluate expressions in this app in the JavaScript and R code alike.
