# GraalVM Demos: Asynchronous Polyglot Programming in GraalVM Using Helidon and JavaScript

This repository contains the code for a demo application for [GraalVM](graalvm.org).
The application demonstrates how multiple JavaScript `Context`s can be executed in parallel to handle asynchronous operations in a [Helidon](https://helidon.io/) application, mixing JavaScript `Promise` and Java `CompletableFuture` objects.

## Prerequisites
* [GraalVM](http://graalvm.org)
* [Maven](https://maven.apache.org)

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

2. Download or clone the repository and navigate into the `js-java-async-helidon` directory:

```
git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos/js-java-async-helidon
```

3. Build the application using Maven:
```
mvn package
```

Now you are all set to run the polyglot Helidon Web service.

## Running the Application
You can run the Helidon HTTP web service with the following command:

```
$GRAALVM_HOME/bin/java -jar target/polyglotHelidonService-SNAPSHOT-jar-with-dependencies.jar
```

The application will create a new HTTP Helidon web service accepting requests on port `8080`.
To send a request, execute the following command:
```
curl http://localhost:8080/greet?request=42
```
To demonstrate error handling, the application will not accept requests with `request` smaller than `42`.
For example, the following requests will return an error message:
```
curl http://localhost:8080/greet?request=41
curl http://localhost:8080/greet?request=foo
```
The Helidon HTTP server uses multiple Java threads.
Each thread runs a private JavaScript context.
To generate concurrent requests that will be handled by multiple threads, any workload generator can be used.
For example, using `wrk`:
```
wrk -c 100 -t 10 -d 100 http://localhost:8080/greet?request=42
```

## A Note About the Application

This is a sample application that for brevity contains reasonably large snippets of code inside the strings.
This is not the best approach for structuring polyglot apps, but the easiest to show in a compact way.
