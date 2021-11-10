# GraalVM Demos

This repository collects demo applications and benchmarks written in Java, JavaScript, R, Ruby, and other JVM languages like Kotlin and Scala.
These programs are illustrating diverse capabilities of [GraalVM](http://graalvm.org).

## Running

Every top level folder here contains demo sources and the instructions on how to run that particular demo are in its README.md.
To run a demo, clone this repository, enter the demo folder, and follow steps in the README.md.

### Running in a Docker Container

Some of the demos (console-based, non-GUI) can be run inside the confinement of a Docker container.
After cloning the repository, but before running any of the demos, do the following:

1. Build the GraalVM demo Docker image of choice:
  ```
  cd docker-images
  ./buildDockerImage.sh "java11-21.2.0"
  ```

  Note: You can find valid tags to specify as parameters [here](https://github.com/graalvm/container/pkgs/container/graalvm-ce).

2. Return to the root directory of the project and run the GraalVM demo Docker container built above:
  ```
  ./runDockerImage.sh "java11-21.2.0"
  ```

Once the Docker container is running, go to the folder of the respective demo, and follow the instructions from its README.md.
Note that GraalVM runtime built with Docker will already contain additional GraalVM components such as Node.JS, Ruby, Python etc., required to run some of the demos.

Running GUI-based applications inside a Docker container requires some intermediate VNC Viewer to access the GUI interface.

1. Download and install any **VNC Viewer**. A number of free or commercial VNC Viewers can be found [here]((https://duckduckgo.com/?q=vnc+viewer+download&ia=web).
2. Wait for the container to be ready, then run VNC Viewer.
3. Log onto http://127.0.0.1:5900 (type it in, in case copy-paste does not work) via the VNC Viewer to access the GUI interface. You will get an `xterm` screen, where you can type in your commands just like the Docker console or any other CLI prompt.

Finally, from the root directory of GraalVM Demos repository, run a Docker container with GraalVM runtime in it:
  ```
  DEMO_TYPE="gui" ./runDockerImage.sh "java11-21.2.0"
  ```

## Tested Compatibility

The demos are normal applications and benchmarks written in Java, Kotlin, JavaScript, etc., so they are compatible with any virtual machine capable of running Java, JavaScript and so on.
You can run it on the stock JVM, Node, etc..
However, these examples were tested and are known to work with GraalVM 21.2.0 builds based on JDK 11.

## Further Information

* [GraalVM website](https://www.graalvm.org)
* [GraalVM on GitHub](https://github.com/oracle/graal/tree/master/compiler)
* [Papers on Truffle](http://ssw.jku.at/Research/Projects/JVM/Truffle.html)
* [Papers on GraalVM](http://ssw.jku.at/Research/Projects/JVM/Graal.html)

## License

Unless specified otherwise, all code in this repository is licensed under the [Universal Permissive License (UPL)](http://opensource.org/licenses/UPL).
Note that the submodule `fastR-examples` which is a reference to the [graalvm/examples](https://github.com/graalvm/examples) repository has a separate license.
