# GraalVM demos: Spring Boot using R packages demo


This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [Maven](https://maven.apache.org/)
* [GraalVM](http://graalvm.org)

## Preparation

Download or clone the repository and navigate into the `spring-r` directory:

```
git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos/spring-r
```

This is a simple Java Spring application that uses GraalVM interop to
load an R script, which uses typical R packages, `ggplot2` in this case.

Before running this example, you need to build the application.
Make sure you export `$GRAALVM_HOME`, add `$GRAALVM_HOME/bin` to the `$PATH`.
Set `JAVA_HOME=$GRAALVM_HOME` to be able to add `graal-sdk.jar` to the classpath.

Install R support for GraalVM:
```
gu install R
```

Install R dependencies. The following command will download the sources for the `ggplot2` dependencies, and place them into GraalVM distribution. (Note: it can take a couple of minutes.)
```
Rscript -e "install.packages(\"ggplot2\")"
```

Run `mvn spring-boot:run`

When the application is ready, open `http://localhost:8080/load`.
