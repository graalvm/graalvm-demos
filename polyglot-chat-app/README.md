# Polyglot Chat Application

This example demonstrates how to integrate Python on GraalVM with a Micronaut application.
The application uses the Gradle build tool.

## Preparation

1. Download or clone the GraalVM demos repository and navigate into the _polyglot-chat-app_ directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/polyglot-chat-app
    ```

2. Download and install the latest GraalPy as described in the [Getting Started guide](https://www.graalvm.org/latest/reference-manual/python/#installing-graalpy). For example on Linux:
   ```bash
   wget https://github.com/oracle/graalpython/releases/download/graal-23.1.1/graalpy-23.1.1-linux-amd64.tar.gz
   tar xzf graalpy-23.1.1-linux-amd64.tar.gz
   ``

3. Create a virtual environment for this demo in the _resources_ directory, and install the required packages:
   ```bash
   graalpy-23.1.1-linux-amd64/bin/graalpy -m venv src/main/resources/venv
   ```
   ```bash
   src/main/resources/venv/bin/graalpy -m pip install nltk
   ```
   ```bash
   src/main/resources/venv/bin/graalpy -c "import nltk; nltk.download('vader_lexicon')"
   ```

4. (Optional) Download and install GraalVM JDK for Java 21 or later to run Python with runtime compilation and to build a native image. 
The demo will work on any OpenJDK distribution, but will be much faster on GraalVM JDK.

## Building and Running the Application:

1. Build application with Gradle:
   ```bash
   ./gradlew run
   ```

2. Navigate to [http://localhost:12345/#/chat/bob](http://localhost:12345/#/chat/bob).

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
   ./build/native/nativeCompile/websocket.chat
   ```

### Learn More 

Learn more about GraalVM polyglot capabilities [here](https://www.graalvm.org/latest/reference-manual/polyglot-programming/).
