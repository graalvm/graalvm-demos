# Polyglot JavaScript Application Mixing Java and R

This is a multi-language JavaScript application mixing Java and R to demonstrate GraalVM's polyglot capabilities.

### Prerequisites

- [GraalVM 22.3.0 or lower](https://www.graalvm.org/22.3/docs/getting-started/)
- [GraalVM Node.js runtime](https://www.graalvm.org/22.3/reference-manual/js/NodeJS/)
- [GraalVM R runtime](https://www.graalvm.org/22.3/reference-manual/r/)

>Note: FastR is no longer under active development and is in maintenance mode. The last released version is 22.3.0.

## Preparation

1. Download and install GraalVM 22.3.0 with the Node.js, and R languages support, using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader).
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) graalvm-ce-java17-22.3.0 -c 'nodejs,R'
    ```
    Follow the post-install message.

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

Open [localhost:3000](http://localhost:3000/) and enjoy the output of the polyglot app.
Play with the source code and restart the application to see what else can you do with the mix of JavaScript, Java, and R.

## Debugging Polyglot Applications

GraalVM 22.3.x supports debugging polyglot applications too.
Add the `--inspect` parameter to the command line: `$JAVA_HOME/bin/node --jvm --polyglot --inspect server.js`. 
Open the URL the application prints at the startup in Chrome browser, and start debugging, set breakpoints, evaluate expressions in this app in the JavaScript and R code alike.

## A Note About the Application

This is a sample application that for brevity contains reasonably large snippets of code inside the strings.
This is not the best approach for structuring polyglot apps, but the easiest to show in a single file.

This demo application is referenced from the blog post [Top 10 Things To Do With GraalVM](https://medium.com/graalvm/graalvm-ten-things-12d9111f307d#656f).