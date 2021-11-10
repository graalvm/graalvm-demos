# GraalVM Demos

This repository collects demo applications and benchmarks written in Java, JavaScript, R, Ruby, and other JVM languages like Kotlin and Scala.
These programs are illustrating diverse capabilities of [GraalVM](http://graalvm.org).

## Running

Clone this repository. Every top level folder here contains demo sources and the instructions on how to run that particular code are in its README.md.

In case you wish to run some of the examples (console-based, non-GUI) inside the confinement of a docker container, then please follow then after cloning the repo but before running any of the demos, please do the below:

Build the **GraalVM** demo docker image of choice:
```
   $ cd docker-images
   $ ./buildDockerImage.sh "java11-21.2.0"
```

Run the GraalVM demo docker container built above:
```
   $ ./runDockerImage.sh "java11-21.2.0"
```


Run a docker container with GraalVM runtime in it (from the root directory of the project) for **GUI-based** apps:

```
   $ DEMO_TYPE="gui" ./runDockerImage.sh "java11-21.2.0"
```

_(One-off: download and install any **VNCViewer**)_
_(Wait for the container to be ready, then run VNCViewer and then log onto http://127.0.0.1:5900 (type it in, in case copy-paste does not work) via the **VNCViewer** to access the GUI interface. You will get an `xterm` screen, where you can type in your commands just like the docker console or any other CLI prompt.)_

Note: Valid tags to specify as parameters, can be found [here](https://github.com/graalvm/container/pkgs/container/graalvm-ce). A number of free or commercial [VNCViewers](https://duckduckgo.com/?q=vnc+viewer+download&ia=web) can be found online and are fairly easy to use.
 

Once the docker container is running, go to the folder of the respective demos, and follow the instructions.

## Tested Compatibility

The demos are normal applications and benchmarks written in Java, Kotlin, JavaScript, etc., so they are compatible with any virtual machine capable of running Java, JavaScript and so on.
You can run it on the stock JVM, Node, etc..

However, these examples were tested and are known to work with GraalVM 21.2.0 builds based on JDK 11.

## Further Information

* [GraalVM website](https://www.graalvm.org)
* [GraalVM on GitHub](https://github.com/oracle/graal/tree/master/compiler)
* [Truffle Javadoc](http://www.graalvm.org/truffle/javadoc/)
* [Truffle on GitHub](https://github.com/oracle/graal/tree/master/truffle)
* [Truffle tutorials and presentations](https://github.com/oracle/graal/blob/master/docs/Publications.md)
* [Papers on Truffle](http://ssw.jku.at/Research/Projects/JVM/Truffle.html)
* [Papers on GraalVM](http://ssw.jku.at/Research/Projects/JVM/Graal.html)

## License

Unless specified otherwise, all code in this repository is licensed under the [Universal Permissive License (UPL)](http://opensource.org/licenses/UPL).
Note that the submodule `fastR-examples` which is a reference to the [graalvm/examples](https://github.com/graalvm/examples) repository has a separate license.
