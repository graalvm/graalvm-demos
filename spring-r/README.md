# GraalVM Demos: Spring Boot Using R Packages Demo

This repository contains the code for a demo application for [GraalVM](graalvm.org).

## Prerequisites
* [GraalVM](http://graalvm.org)
* R support

## Preparation

This is a simple Java Spring application that uses GraalVM interoperability to load an R script, which uses typical R packages, `lattice` in this case.

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

2. Install R support for GraalVM:
  ```bash
  gu install R
  ```

3. Download or clone the repository and navigate into the `spring-r` directory:
  ```bash
  git clone https://github.com/graalvm/graalvm-demos
  cd graalvm-demos/spring-r
  ```

4. Run the example:
  ```bash
  mvn spring-boot:run -Dgraalvm.version=21.2.0
  ```

Replace "21.2.0" with your version of GraalVM.
When the application is ready, open `http://localhost:8080/load`.

> Note: It may take 2-3 minutes to generate the plot.
