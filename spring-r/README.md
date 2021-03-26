# GraalVM Demos: Spring Boot Using R Packages Demo

This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [Maven](https://maven.apache.org/)
* [GraalVM](http://graalvm.org)

## Preparation

This is a simple Java Spring application that uses GraalVM interoperability to
load an R script, which uses typical R packages, `lattice` in this case.

1. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$GRAALVM_HOME`, and add `$GRAALVM_HOME/bin` to the `PATH` environment variable.

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

2. Install R support for GraalVM:
```
gu install R
```

3. Download or clone the repository and navigate into the `spring-r` directory:

```
git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos/spring-r
```

4. Run the example:
```
mvn spring-boot:run -Dgraalvm.version=21.1.0
```
Replace "21.1.0" with your version of GraalVM.
When the application is ready, open `http://localhost:8080/load`.
