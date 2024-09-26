# GraalPy JBang QRCode Demo

This demo illustrates how GraalPy can be used to embed [the `qrcode` Python package](https://pypi.org/project/qrcode/) in a script that runs on [JBang](https://www.jbang.dev/).

A minimal Java application that embeds [the official Python library for the OpenAI API](https://github.com/openai/openai-python) with GraalPy.

## Preparation

Install GraalVM for JDK 23 and set the value of `JAVA_HOME` accordingly.
We recommend using [SDKMAN!](https://sdkman.io/). (For other download options, see [GraalVM Downloads](https://www.graalvm.org/downloads/).)

```bash
sdk install java 23-graal
```

Afterward, [install `jbang`](https://www.jbang.dev/download/), for with:

```bash
sdk install jbang
```

## Run the Application

To start the demo, run:

```bash
jbang run QRCodeMaker.java "Hello from GraalPy!"
```

## Implementation Details

[qrcode.java](qrcode.java) defines required dependencies on GraalPy, which also allows to depend on Python packages.
`//PIP qrcode==7.4.2` defines the dependency on the `qrcode` Python package.
The `main()` method checks the arguments and then creates a new `Context`.
It then fetches the `qrcode` Python module and maps it to a `QRCode` Java interface.
Afterward, it uses the `qrcode` module as illustrated in [this example](https://github.com/lincolnloop/python-qrcode/tree/v7.4.2?tab=readme-ov-file#examples) and using Java interfaces.
Finally, it prints the result to `System.out`.
