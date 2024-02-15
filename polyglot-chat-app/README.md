# Polyglot Chat Application

This example demonstrates how to integrate Python in a Micronaut Java application using the Gradle build tool.
The application uses the [Natural Language Toolkit (nltk)](https://www.nltk.org/) module to analyze the sentiment of user input.
The example also shows how to create a native executable from the application using GraalVM.

## Preparation

1. Download or clone the GraalVM demos repository and navigate into the _polyglot-chat-app_ directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/polyglot-chat-app
    ```

2. Download and install the latest GraalPy as described in the [Getting Started guide](https://www.graalvm.org/latest/reference-manual/python/#installing-graalpy). For example:
   ```bash
   pyenv install graalpy-23.1.2
   pyenv shell graalpy-23.1.2
   ```

3. Create a virtual environment for the demo in the _resources_ directory, activate it, install the required package, and download a lexicon:
   ```bash
   graalpy -m venv src/main/resources/venv
   ```
   ```bash
   source src/main/resources/venv/bin/activate
   ```
   ```bash
   graalpy -m pip install nltk
   ```
   ```bash
   graalpy -c "import nltk; nltk.download('vader_lexicon')"
   ```

4. The demo will work with any OpenJDK distribution, but will be much faster on [GraalVM JDK for Java 21](https://www.graalvm.org/downloads/). 


## Building and Running the Application

1. Build and run the application using Gradle:
   ```bash
   ./gradlew run
   ```

2. Navigate to [http://localhost:12345/#/chat/bob](http://localhost:12345/#/chat/bob).

    You can connect from multiple browsers and chat via websockets.
    The Python code loads a language model in the background&mdash;this can take up to 5 minutes.
    Once it is ready, it analyzes the sentiments of messages and add an emoji to each message to indicate the feelings conveyed.
    A chat may look like this (newest message at the top):

    ```
    [bob 😀] awesome, GraalVM and GraalPy rock!
    [bob 🫥] are we done yet?
    [bob 💬] Joined!
    ```

## (Optional) Building a Native Executable

> Note: this requires [GraalVM JDK for Java 21](https://www.graalvm.org/downloads/) or later.

The application can be compiled ahead-of-time to a native executable using GraalVM Native Image.
The Python code must be copied to the _build/_ directory that is kept next to the native executable.

1. Build a native executable using Micronaut AOT support:
   ```bash
   ./gradlew nativeCompile
   ```

2. Copy the _venv_ directory into the output _resources_ directory:
   ```bash
   cp -R src/main/resources/venv/ build/native/nativeCompile/resources/python/
   ```

3. Run the native executable:
   ```bash
   ./build/native/nativeCompile/websocket.chat
   ```

### Learn More 

Learn more about GraalVM polyglot capabilities [here](https://www.graalvm.org/latest/reference-manual/polyglot-programming/).
