# FastR Examples

A collection of examples and use-cases for FastR.

FastR is a GraalVM based implementation of the R programming language, that provides significant improvements in the performance of R code,
the embedding of the R execution engine into Java applications, and the ability to interface with other GraalVM and JVM languages including Python, Java, and Scala. 
FastR is also able to utilize the [tooling support](https://medium.com/graalvm/analyzing-the-heap-of-graalvm-polyglot-applications-b9963e68a6a) provided by the
[GraalVM ecosystem](https://medium.com/graalvm/graalvm-ten-things-12d9111f307d).

* [FastR Java UI](./fastr_javaui/README.md) is a Java based Swing desktop UI application showing visualization interactively generated an by R script.
* [Python Interop](./python_exp) demonstrates how to access functionality of R packages from within Python (`ks.test` and lattice) using the built-in language interoperability feature of GraalVM.
* [rJava Benchmark](./r_java_bench) shows how fast rJava can be on FastR (spoiler: orders of magnitude faster).
* [FastR Embedding](./r_java_embedding) show how to embed FastR into Java applications and pass Java objects to R scripts like if they were native R objects (e.g. R data frame).

## Setup

In order to run the examples, the latest GraalVM must be installed.
It can be downloaded from the [GraalVM homepage](http://www.graalvm.org/downloads/).
The examples work on both the Community and Enterprise edition of GraalVM.

Once downloaded, extract the archive, set the `GRAALVM_DIR` environment variable to point to the graalvm directory, 
and install additional languages using following commands:

```
$GRAALVM_DIR/bin/gu install R
$GRAALVM_DIR/bin/gu install python
```

## Further information

* [GraalVM homepage](http://graalvm.org)
* [FastR reference manual](http://www.graalvm.org/docs/reference-manual/languages/r/)
* [FastR on Github](https://github.com/oracle/fastr)

## License

All the examples are licensed under the [Universal Permissive License (UPL)](http://opensource.org/licenses/UPL).

## Troubleshooting

A typical problem is proxy set-up: verify that you have the `http_proxy`, `https_proxy`, and `no_proxy` environment variables set properly.


