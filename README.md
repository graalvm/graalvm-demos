# GraalVM Demos

This repository collects demo applications and benchmarks written in Java, JavaScript, R, Ruby, and other JVM languages like Kotlin and Scala. These programs are illustrating diverse capabilities of [GraalVM](http://graalvm.org).

## Running

Clone this repository. Every top level folder here contains a demo sources and the instructions on how to run that particular code are in its README.md.

## Tested Compatibility

The demos are normal applications & benchmarks written in Java, Kotlin, JavaScript, etc., so they are compatible with any virtual machine capable of running Java, JavaScript and so on. You can run it on the stock JVM, Node, etc..

However, these were tested and are known to work with:
* GraalVM 1.0.0 RC9

## Further Information

* [GraalVM]( http://www.oracle.com/technetwork/oracle-labs/program-languages/overview) on the Oracle Technology Network
* [Graal on Github](https://github.com/oracle/graal/tree/master/compiler)
* [Truffle JavaDoc](http://www.graalvm.org/truffle/javadoc/)
* [Truffle on Github](https://github.com/oracle/graal/tree/master/truffle)
* [Truffle Tutorials and Presentations](https://github.com/oracle/graal/blob/master/docs/Publications.md)
* [Papers on Truffle](http://ssw.jku.at/Research/Projects/JVM/Truffle.html)
* [Papers on Graal](http://ssw.jku.at/Research/Projects/JVM/Graal.html)

## License

Unless specified otherwise all code in this repository is licensed under the [Universal Permissive License (UPL)](http://opensource.org/licenses/UPL). Note that the submodule `fastR-examples` which is a reference to the
[graalvm/examples](https://github.com/graalvm/examples) repository has a separate license.
