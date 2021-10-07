# Micronaut Demo with Python for Data Visualization

This repository contains the code of the Micronaut application that uses Python for data visualization.

## Prerequisites
* [GraalVM](https://www.graalvm.org/) (Java 11 based)
* [Python support](https://www.graalvm.org/reference-manual/python/#installing-python)

## Preparation

1. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$JAVA_HOME` and add `$JAVA_HOME/bin` to the `PATH` environment variable.

  On Linux:
  ```bash
  export JAVA_HOME=/home/${current_user}/path/to/graalvm
  export PATH=$JAVA_HOME/bin:$PATH
  ```
  On macOS:
  ```bash
  export JAVA_HOME=/Users/${current_user}/path/to/graalvm/Contents/Home
  export PATH=$JAVA_HOME/bin:$PATH
  ```
  On Windows:
  ```bash
  setx /M JAVA_HOME "C:\Progra~1\Java\<graalvm>"
  setx /M PATH "C:\Progra~1\Java\<graalvm>\bin;%PATH%"
  ```

2. Add Python support. GraalVM comes with `gu` which is a command line utility to install additional functionalities. To add Python, run this single command:
  ```bash
  gu install python
  ```

3. Download or clone the repository and navigate to the `mn-python` directory:
  ```bash
  git clone https://github.com/graalvm/graalvm-demos
  cd graalvm-demos/mn-python
  ```

## Prepare the environment

Create a virtual environment for Python dependencies:
```
cd mn-python
graalpython -m venv env
```

Download the `pygal` module wheel file to install it from source:
```
wget https://files.pythonhosted.org/packages/5f/b7/201c9254ac0d2b8ffa3bb2d528d23a4130876d9ba90bc28e99633f323f17/pygal-2.4.0-py2.py3-none-any.whl
./env/bin/pip install ./pygal-2.4.0-py2.py3-none-any.whl
```

## Run the example:
```
gradle run
```
When the application is ready, open `http://localhost:8080/`.
