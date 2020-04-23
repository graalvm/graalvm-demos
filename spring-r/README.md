# GraalVM demos: Spring Boot using R packages demo

This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [Maven](https://maven.apache.org/)
* [GraalVM](http://graalvm.org)

## Preparation


This is a simple Java Spring application that uses GraalVM interoperability to
load an R script, which uses typical R packages, `ggplot2` in this case.

1. Download or clone the repository and navigate into the `spring-r` directory:
```
git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos/spring-r
```
2. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$GRAALVM_HOME` and add `$GRAALVM_HOME/bin` to the `PATH` environment variable. Set `JAVA_HOME=$GRAALVM_HOME` to be able to add `graal-sdk.jar` to the classpath.

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
3. Install R support for GraalVM:
```
gu install R
```
4. Install R dependencies. The following command will download the sources for the `ggplot2` dependencies, and place them into GraalVM distribution. (Note: it can take a couple of minutes.)
```
Rscript -e "install.packages(\"ggplot2\")"
```
5. Run the example:
```
mvn spring-boot:run
```
When the application is ready, open `http://localhost:8080/load`.
