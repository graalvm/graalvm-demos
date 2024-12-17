# Asynchronous Polyglot Programming in GraalVM Using Helidon and JavaScript

This is a polyglot Helidon HTTP web service that demonstrates how multiple JavaScript `Context`s can be executed in parallel to handle asynchronous operations with [Helidon](https://helidon.io/), mixing JavaScript `Promise` and Java `CompletableFuture` objects.

## Preparation

1. Download and install the latest GraalVM JDK using [SDKMAN!](https://sdkman.io/).
    ```bash
    sdk install java 21.0.5-graal
    ```

2. Download or clone the repository and navigate into the `js-java-async-helidon` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/archive/js-java-async-helidon
    ```

3. Build the application using Maven:
    ```bash
    mvn clean package
    ```

Now you are all set to run the polyglot Helidon web service.

## Running the Application

You can run this Helidon application with the following command:
```bash
mvn exec:exec
```

The application accepts requests on port `8080`.
Open [http://localhost:8080/greet?request=42](http://localhost:8080/greet?request=42) in the browser to send a request.

To demonstrate error handling, the application will not accept requests with `request` smaller than `42`.
For example, the following requests will return an error message:
```bash
curl http://localhost:8080/greet?request=41
```
```bash
curl http://localhost:8080/greet?request=foo
```

The Helidon HTTP server uses multiple Java threads.
Each thread runs a private JavaScript context.
To generate concurrent requests that will be handled by multiple threads, any workload generator can be used.
For example, using `wrk`:
```bash
wrk -c 100 -t 10 -d 100 http://localhost:8080/greet?request=42
```

## A Note About the Application

This is a sample application that, for brevity, contains reasonably large snippets of code inside the strings.
This is not the best approach for structuring polyglot apps, but the easiest to show in a compact way.

Read more about asynchronous programming across multiple languages in this post [Asynchronous polyglot programming with Java and JavaScript on GraalVM](https://medium.com/graalvm/asynchronous-polyglot-programming-in-graalvm-javascript-and-java-2c62eb02acf0).