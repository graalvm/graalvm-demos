# GraalVM demos: Micronaut app with Python for Data visualization

This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [Gradle](https://gradle.org/)
* [GraalVM](http://graalvm.org) (jdk11 based)
* Python support installed 

## Preparation

This demo should work on Linux. Didn't test other operating systems.  

On Linux:
```
export GRAALVM_HOME=/home/${current_user}/path/to/graalvm
export PATH=$GRAALVM_HOME/bin:$PATH
```

Install python support for GraalVM:
```
gu install python
```

## Prepare the application

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

