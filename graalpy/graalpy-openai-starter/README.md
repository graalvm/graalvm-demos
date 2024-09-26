# GraalPy OpenAI Starter

A minimal Java application that embeds [the official Python library for the OpenAI API](https://github.com/openai/openai-python) with GraalPy.

## Preparation

Install GraalVM for JDK 23 and set the value of `JAVA_HOME` accordingly.
We recommend using [SDKMAN!](https://sdkman.io/). (For other download options, see [GraalVM Downloads](https://www.graalvm.org/downloads/).)

```bash
sdk install java 23-graal
```

This project also requires that an OpenAI API key is set via the `OPENAI_API_KEY` environment variable.
For more information on how to generate and set such a key, check out [the Developer quickstart from OpenAI](https://platform.openai.com/docs/quickstart/create-and-export-an-api-key).

## Run the Application Using Maven

To build and test the demo, run:

```bash
./mvnw test
```

To execute the main method, run:

```bash
./mvnw exec:java -Dexec.args="'Say Hello from GraalPy'"
```

## Run the Application Using Gradle

To build and test the demo, run:

```bash
./gradlew test
```

To execute the main method, run:

```bash
./gradlew run --args="'Say Hello from GraalPy'"
```

## Implementation Details

The [App.java](src/main/java/com/example/App.java) embeds a `create_chat_completion()` function based on [the official usage example](https://github.com/openai/openai-python?tab=readme-ov-file#usage).
The Java text block that contains the Python code is annotated with `// language=python`, which triggers a [language injection in IntelliJ IDEA](https://www.jetbrains.com/help/idea/using-language-injections.html).
The `create_chat_completion()` function is returned when the Python code is evaluated, and mapped to `CreateChatCompletionFunction`, a `FunctionalInterface` from `String` to `ChatCompletion`.
Afterward, the Python function is called with `userInput` from Java.
The interfaces `ChatCompletion`, `Choice`, and `ChatComplectionMessage` are ported from [the official API](https://github.com/openai/openai-python/blob/main/api.md), and only contain the functionality exercised by the example code.
These interfaces can be extended appropriately when needed.

Moreover, the [_pom.xml_](pom.xml) contains the `openai` package and all its transitive dependencies, and locks their version.
This is recommended because it provides a similar experience to Maven (all versions are well-defined), while `pip`, the package installer for Python, also supports version ranges (versions are defined at compile time).
In particular, the `jiter` and `pydantic_core` make use of native extensions which must be built for GraalPy.
For the selected versions of these two packages, there are pre-built binary wheels for Linux x86_64 in the [GraalPy PyPI repository](https://www.graalvm.org/python/wheels/).
This means that on Linux x86_64 machines, the native extensions do not need to be built from source and are instead fetched from the PyPI repository.

> Note: Although `jiter`, `pydantic_core`, and many other Python packages that use native extensions work with GraalPy, their use is currently considered experimental.
> Also note that when using multiple contexts, only one context can load and access a native extension at a time.
> We are exploring approaches to support multi-context setups with native extensions.
