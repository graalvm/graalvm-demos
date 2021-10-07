# GraalVM Demos: FunctionGraph Demo - Polyglot JavaScript, Java, R application

This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites

- GraalVM for Java 11 or higher
- Node.js support
- R support

## Preparation

1. Download and [install GraalVM](https://www.graalvm.org/docs/getting-started/#install-graalvm). You can download the latest GraalVM [here](https://www.graalvm.org/downloads/).

2. Add Node.js and R runtime support. GraalVM comes with `gu` which is a command line utility to install and manage additional functionalities, and to install Node.js and R, run this single command:
  ```bash
  <graalvm>/bin/gu install nodejs R
  ```
  Check the Node.js version:
  ```bash
  node -v
  ```

3. Download or clone the repository and navigate into the `functionGraphDemo` directory:
  ```
  git clone https://github.com/graalvm/graalvm-demos
  cd graalvm-demos/functionGraphDemo
  ```
4. Build the application by running `npm install`.

Now you are all set to run the polyglot JavaScript, Java, R application.

## Running the application
To run the application, you need to execute the `server.js` file.
You can run it with the following command, `<graalvm>` is path to the GraalVM installation directory.

```
<graalvm>/bin/node --jvm --polyglot server.js
```

If you would like to run the benchmark on a different instance of Node, you can run it with whatever `node` you have.
However, usually the polyglot capability will not be supported and the app will not run successfully.

Open [localhost:8088](http://localhost:8088/) and enjoy the output.
Play with the source code, for example, type `1 * 7` in a function field and press "Draw Function".
Restart the application to see what else can you do with the mix of JavaScript, Java, and R.

## Debugging polyglot applications
GraalVM supports [debugging polyglot applications](https://www.graalvm.org/tools/chrome-debugger/) too, add the `--inspect` parameter to the command line, open the URL the application prints at the startup in Chrome browser.
You can debug, set breakpoints, evaluate expressions in this app in the JavaScript and R code alike.
