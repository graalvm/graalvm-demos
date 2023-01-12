# Spring Boot Using R Packages Demo

This repository contains the code for a demo application for [GraalVM](graalvm.org).

### Prerequisites

* [GraalVM](http://graalvm.org)
* [R support](https://www.graalvm.org/latest/reference-manual/r/)

## Preparation

This is a simple Java Spring application that uses GraalVM interoperability to load an R script, which uses typical R packages, `lattice` in this case.

1. Download and install the latest GraalVM JDK with R support using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) -c 'R'
    ```

2. Download or clone the repository and navigate into the `spring-r` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/spring-r
    ```

4. Run the example:
    ```bash
    mvn spring-boot:run -Dgraalvm.version=22.1.0
    ```

Replace "22.1.0" with your version of GraalVM.
When the application is ready, open `http://localhost:8080/load`.

> Note: It may take 2-3 minutes to generate the plot.
