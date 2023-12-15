# Spring Boot Using R Packages Demo

This repository contains the code for a demo application for [GraalVM](graalvm.org).

### Prerequisites

- [GraalVM 22.2.0  or lower](https://www.graalvm.org/)
- [R runtime](https://www.graalvm.org/22.3/reference-manual/r/)

>Note: FastR is no longer under active development and is in maintenance mode. The last released version is 22.3.0.

## Preparation

This is a simple Java Spring application that uses GraalVM interoperability to load an R script, which uses typical R packages, `lattice` in this case.

1. Download and install GraalVM 22.2.0 with the R language support, using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader).
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) graalvm-ce-java17-22.2.0 -c 'R'
    ```
    Follow the post-install message.

2. Download or clone the repository and navigate into the `spring-r` directory:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/spring-r
    ```

4. Run the example:
    ```bash
    mvn spring-boot:run -Dgraalvm.version=22.2.0
    ```

Replace "22.1.0" with your version of GraalVM.
When the application is ready, open `http://localhost:8080/load`.

> Note: It may take 2-3 minutes to generate the plot.
