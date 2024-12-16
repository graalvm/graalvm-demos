# Polyglot Chat Application

This example demonstrates how to integrate Python on GraalVM with a Micronaut application.
The application uses the Gradle build tool.

### Prerequisites
- [Native Image](https://www.graalvm.org/latest/reference-manual/native-image/)
- [Python support](https://www.graalvm.org/latest/reference-manual/python/)

## Preparation

1. Download the latest GraalPy as described on [https://www.graalvm.org/python/](https://www.graalvm.org/python/). For example on Linux:
    ```bash
    wget https://github.com/oracle/graalpython/releases/download/graal-23.1.1/graalpy-23.1.1-linux-amd64.tar.gz
    tar xzf graalpy-23.1.1-linux-amd64.tar.gz
    ```

2. Install the required packages for this demo into the _resources_ directory:
   ```bash
   graalpy-23.1.1-linux-amd64/bin/graalpy -m venv src/main/resources/venv
   src/main/resources/venv/bin/graalpy -m pip install nltk
   ```

3. Optional: Download and install GraalVM JDK for Java 21 or later to run Python with runtime compilation and to build a native image.
   The demo will work on any OpenJDK distribution, but will be much faster on GraalVM JDK.

## Building and Running the application:

1. Build application with Gradle:
    ```bash
    ./gradlew run
    ```

2. Navigate to http://localhost:12345/#/chat/bob

    You can connect from multiple browsers and chat via websockets.
    The Python code will load a language model in the background.
    Once it is ready, it will analyse the sentiments of all messages and add an emoji to the message to indicate the feelings conveyed.
    A chat may look like this (newest message at the top):

    ```
    [bob 😀] awesome, GraalVM and GraalPy rock!
    [bob 🫥] are we done yet?
    [bob 💬] still loading the sentiment model I believe
    ```

## Building a Native Image

The application can be AOT compiled using GraalVM Native Image.
The Python code has to be shipped in a _resources_ directory that is kept next to the native executable.

1. Build a native executable with the Micronaut AOT support:
   ```bash
   ./gradlew nativeCompile
   ```

2. Copy the venv into the output _resources_ directory:
   ```bash
   cp -R src/main/resources/venv/ build/native/nativeCompile/resources/python/
   ```

3. Run the native executable:
   ```bash
   build/native/nativeCompile/websocket.chat
   ```

### Learn More 

Learn more about GraalVM polyglot capabilities [here](https://www.graalvm.org/reference-manual/embed-languages/).