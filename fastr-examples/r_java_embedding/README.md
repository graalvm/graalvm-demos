
```shell
The whole data frame printed in R:
  id    name language
1  1 Florian   Python
2  2   Lukas        R
3  3    Mila     Java
4  4   Paley      Coq
5  5  Stepan       C#
6  6   Tomas     Java
7  7  Zbynek    Scala
---------

Filter out users with ID>2:
  id   name language
3  3   Mila     Java
4  4  Paley      Coq
5  5 Stepan       C#
6  6  Tomas     Java
7  7 Zbynek    Scala
---------

How many users like Java: 2
```

# Embedding R as a scripting language into JVM applications

This demo shows how to embed FastR into Java application and how to share data between Java and R. 
The Java program only needs to implement few proxies to transform the data into the shape of a 
data frame and then the R code can work with the data like with any other data frame.
There is no need to copy or marshal the data between the two runtimes.

## Setup

Follow the instructions from the top level [README](../README.md) to install and setup GraalVM.
Build the sources with `./build.sh`.

## Run

The example can be executed using the `run.sh` script.

