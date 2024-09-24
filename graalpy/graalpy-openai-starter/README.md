# GraalPy OpenAI Starter

A minimal Java application that embeds [the official Python library for the OpenAI API](https://github.com/openai/openai-python) with GraalPy.

## Preparation

Install GraalVM for JDK 23 and set the value of `JAVA_HOME` accordingly.
We recommend using [SDKMAN!](https://sdkman.io/). (For other download options, see [GraalVM Downloads](https://www.graalvm.org/downloads/).)

```bash
sdk install java 23-graal
```

## Run the Application Using Maven

To build and test the demo, run:

```bash
./mvnw test
```

To execute the main method, run:

```bash
./mvnw exec:java
```

## Run the Application Using Gradle

To build and test the demo, run:

```bash
./gradlew test
```

To execute the main method, run:

```bash
./gradlew run
```

## Implementation Details

Note that the [_pom.xml_](pom.xml) contains the `openai` package and all of its transitive dependencies, and locks their version.
This is recommended because it provides a similar experience to Maven (all versions are well-defined), while `pip`, the package installer for Python, also supports version ranges (versions are defined at compile time).
In particular, the `jiter` and `pydantic_core` make use of native extensions which must be built for GraalPy.
For the selected versions of these two packages, there are pre-built binary wheels for Linux x86_64 in the [GraalPy PyPI repository](https://www.graalvm.org/python/wheels/).
This means that on Linux x86_64 machines, the native extensions do not need to be built from source and are instead fetched from the PyPI repository.
