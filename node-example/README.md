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

Build the application before running. The application requires python libraries for plotting the graph. You can manually execute all the commands from `build.sh` file line-by-line, but that is more convenient to execute `build.sh` script as it is shown below.

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

This application uses [matplotnode](https://www.npmjs.com/package/matplotnode) package to plot the graph. This package requires Python 2.7 and matplotlib installed. Scripts to install them and their dependencies are in `build.sh` file.