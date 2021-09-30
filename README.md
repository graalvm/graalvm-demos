# GraalVM Demos

This repository collects demo applications and benchmarks written in Java, JavaScript, R, Ruby, and other JVM languages like Kotlin and Scala. These programs are illustrating diverse capabilities of [GraalVM](http://graalvm.org).

## Running

Clone this repository. Every top level folder here contains a demo sources and the instructions on how to run that particular code are in its README.md.

In case you wish to run some of the examples (console-based, non-GUI) inside the confinement of a docker container, then please follow then after cloning the repo but before running any of the demos, please do the below:

Build the **GraalVM** demo docker image of choice:
```
   $ cd docker-images
   $ FULL_GRAALVM_VERSION="21.2.0-java11-all" ./buildDockerImage.sh
```

Run the GraalVM demo docker container built above:
```
   $ FULL_GRAALVM_VERSION="21.2.0-java11-all" ./runDockerImage.sh
```

Note: Please specify a value tag with `FULL_GRAALVM_VERSION`, valid tags can be found [here](https://hub.docker.com/r/findepi/graalvm/tags). 

Once the docker container is running, go to the folder of the respective demos, and follow the instructions.

## Tested Compatibility

The demos are normal applications and benchmarks written in Java, Kotlin, JavaScript, etc., so they are compatible with any virtual machine capable of running Java, JavaScript and so on. You can run it on the stock JVM, Node, etc..

However, these examples were tested and are known to work with GraalVM 21.0.0 builds based on JDK 8 and on JDK 11.

## Further Information

* [GraalVM website](https://www.graalvm.org)
* [GraalVM on GitHub](https://github.com/oracle/graal/tree/master/compiler)
* [Truffle Javadoc](http://www.graalvm.org/truffle/javadoc/)
* [Truffle on GitHub](https://github.com/oracle/graal/tree/master/truffle)
* [Truffle tutorials and presentations](https://github.com/oracle/graal/blob/master/docs/Publications.md)
* [Papers on Truffle](http://ssw.jku.at/Research/Projects/JVM/Truffle.html)
* [Papers on GraalVM](http://ssw.jku.at/Research/Projects/JVM/Graal.html)

## License

Unless specified otherwise, all code in this repository is licensed under the [Universal Permissive License (UPL)](http://opensource.org/licenses/UPL). Note that the submodule `fastR-examples` which is a reference to the
[graalvm/examples](https://github.com/graalvm/examples) repository has a separate license.
