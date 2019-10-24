# GraalVM demos: Graal.js application


This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [GraalVM](http://graalvm.org)

## Preparation

Download or clone the repository and navigate into the `node-example` directory:

```
git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos/node-example
```

Build the application before running. You can manually execute `npm install plotly`, but there's also a `build.sh` script included for your convenience.

Execute:
```
bash build.sh
```

After this export the GraalVM home directory as the `$GRAALVM_HOME`, e.g.:

```
export GRAALVM_HOME=/home/${USER}/graalvm/graalvm-$GRAALVM_VERSION
```

## Running the application

To run the application, you need to execute the `taylor.js` file. You can run it with the following command (or run the `run.sh` script):

```
$GRAALVM_HOME/bin/node taylor.js
```

Open [localhost:5000](localhost:5000) in your browser to see the output of the example app.

The application tabulates sin(x) function using Taylor series, plots a graph of this function and responses to a client with the image of the graph.

## Benchmarking

You can use `ab` (Apache benchmark) tool for benchmarking while the server is running this way:

```
ab -n 50 localhost:5000/
```

This will perform 50 requests to the server and print results of benchmarking.

## Notes about the application

This application uses [Plotly](https://plot.ly) library to plot the graph. Plotly provides API for plotting graphs, and requires username and API key indicated.

To get username and API key, proceed to the [Plotly](https://chart-studio.plot.ly/settings/api#/) site, log in or sign up, go to Settings->API Keys and click on `Regenerate Key` button. Then copy your username and API key and paste them there, on the top of the `taylor.js` file:

```
const plotly = require('plotly')('username', 'api_key');
```

This API key can be used 1000 times per 24h.