# FunctionGraph Demo - Polyglot JavaScript, Java, R Application

This is a multi-language application mixing JavaScript, Java, and R to demonstrate GraalVM polyglot capabilities. 

### Prerequisites

- [GraalVM 22.3.0 or lower](https://www.graalvm.org/)
- [GraalVM Node.js runtime](https://www.graalvm.org/22.3/reference-manual/js/NodeJS/)
- [GraalVM Ruby runtime](https://www.graalvm.org/22.3/reference-manual/ruby/)
- [GraalVM R runtime](https://www.graalvm.org/22.3/reference-manual/r/)

>Note: FastR is no longer under active development and is in maintenance mode. The last released version is 22.3.0.

## Preparation

1. Download and install GraalVM 22.3 with the Node.js, R, and Ruby languages support, using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader).
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) graalvm-ce-java17-22.2.0 -c 'nodejs,R,ruby'
    ```
    Follow the post-install message.

2. Download or clone GraalVM demos repository and navigate into the `functionGraphDemo` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/archive/functionGraphDemo
    ```

3. Build the application by running `npm install`.
    ```bash
    $JAVA_HOME/bin/npm install
    ```

Now you are all set to run this polyglot application.

## Running the Application

To run the application, you need to execute the `server.js` file providing  `--jvm` and `--polyglot` flags:

```bash
$JAVA_HOME/bin/node --jvm --polyglot server.js
```

Open [localhost:8088](http://localhost:8088/) and enjoy the output.
Play with the source code, for example, type `1 * 7` in a function field and press "Draw Function".
Restart the application to see what else can you do with the mix of JavaScript, Java, and R.

## Debugging Polyglot Applications

GraalVM supports [debugging polyglot applications](https://www.graalvm.org/22.3/tools/chrome-debugger/) too, add the `--inspect` parameter to the command line, open the URL the application prints at the startup in Chrome browser. You can debug, set breakpoints, evaluate expressions in this app in the JavaScript and R code alike.