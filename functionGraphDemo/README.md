# FunctionGraph Demo - Polyglot JavaScript, Java, R application

This repository contains the code for a demo application for [GraalVM](graalvm.org).

### Prerequisites

- GraalVM
- [Node.js support](https://www.graalvm.org/latest/reference-manual/js/NodeJS/)
- [Ruby support](https://www.graalvm.org/latest/reference-manual/ruby/)
- [R support](https://www.graalvm.org/latest/eference-manual/r/)

## Preparation

1. Download and install the latest GraalVM JDK with Node.js, R, and Ruby languages support, using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader).
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) -c 'nodejs,R,ruby'
    ```

2. Download or clone GraalVM demos repository and navigate into the `functionGraphDemo` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/functionGraphDemo
    ```

3. Build the application by running `npm install`.
    ```bash
   $JAVA_HOME/bin/npm install
    ```

Now you are all set to run the polyglot JavaScript, Java, R application.

## Running the Application

To run the application, you need to execute the `server.js` file providing  `--jvm` and `--polyglot` flags:

```bash
$JAVA_HOME/bin/node --jvm --polyglot server.js
```

If you would like to run the benchmark on a different instance of Node, you can run it with whatever `node` you have.
However, usually the polyglot capability will not be supported and the app will not run successfully.

Open [localhost:8088](http://localhost:8088/) and enjoy the output.
Play with the source code, for example, type `1 * 7` in a function field and press "Draw Function".
Restart the application to see what else can you do with the mix of JavaScript, Java, and R.

## Debugging Polyglot Applications

GraalVM supports [debugging polyglot applications](https://www.graalvm.org/tools/chrome-debugger/) too, add the `--inspect` parameter to the command line, open the URL the application prints at the startup in Chrome browser.
You can debug, set breakpoints, evaluate expressions in this app in the JavaScript and R code alike.
