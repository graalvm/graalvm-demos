# GraalVM Demos: Polyglot JavaScript, Java, R application

This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* GraalVM
* R support
* Node.js support

## Preparation

1. Download and install the latest GraalVM JDK with Node.js and R support using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) -c 'nodejs,R'
    ```

2. Download or clone the repository and navigate into the `polyglot-javascript-java-r` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/polyglot-javascript-java-r
    ```

3. Build the benchmark. You can manually execute `npm install`, but there is also a `build.sh` script included for your convenience:
    ```bash
    ./build.sh
    ```
Now you are all set to run this polyglot JavaScript, Java, and R application.

## Running the Application

To run the application, you need to execute the `server.js` file. You can run it with the following command (or run the `run.sh` script):
```bash
$JAVA_HOME/bin/node --jvm --polyglot server.js
```

If you would like to run the benchmark on a different instance of Node, you can run it with whatever `node` you have.
However, usually the polyglot capability won't be supported and the app won't run successfully.

Open [localhost:3000](http://localhost:3000/) and enjoy the output of the polyglot app.
Play with the source code and restart the application to see what else can you do with the mix of JavaScript, Java, and R.

## Debugging Polyglot Applications

GraalVM supports debugging polyglot applications too.
Add the `--inspect` parameter to the command line, open the URL the application prints at the startup in Chrome browser, and start debugging, set breakpoints, evaluate expressions in this app in the JavaScript and R code alike.

## A Note About the Application

This is a sample application that for brevity contains reasonably large snippets of code inside the strings.
This is not the best approach for structuring polyglot apps, but the easiest to show in a single file.
