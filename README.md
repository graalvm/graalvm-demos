# GraalVM Demos

This repository contains demo applications and benchmarks written in Java, JavaScript, Python, and other languages.
These applications illustrate the diverse capabilities of [GraalVM](http://graalvm.org). 

The demos are sorted by a framework, by a programming language, or by a technology.
Each directory contains demo sources; the instructions on how to run a particular demo are in its _README.md_ file.
To get started, clone or download this repository, enter the demo directory, and follow steps in the _README.md_ file.


```
git clone https://github.com/graalvm/graalvm-demos.git
cd graalvm-demos
```

### GraalVM JDK and Native Image

<table>
  <thead>
    <tr>
      <th align="left">Name</th>
      <th align="left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="left" width="30%"><a href="/tiny-java-containers/">tiny-java-containers</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/tiny-java-containers.yml"><img alt="tiny-java-containers" src="https://github.com/graalvm/graalvm-demos/actions/workflows/tiny-java-containers.yml/badge.svg" /></a>
      <td align="left" width="70%">Demonstrates how to build very small Docker container images with GraalVM Native Image and various lightweight base images. <br><strong>Technologies: </strong> Native Image, musl libc<br><strong>Reference: </strong><a href="https://www.graalvm.org/22.0/reference-manual/native-image/StaticImages/">Static and Mostly Static Images</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-image/hello-world/">native-image/hello-world</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-hello-world.yml"><img alt="native-image/hello-world" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-hello-world.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to build native executables from a class file and a JAR file from the command line <br><strong>Technologies: </strong> Native Image <br><strong>Reference: </strong><a href="https://www.graalvm.org/latest/reference-manual/native-image/#build-a-native-executable-using-the-native-image-tool">Native Image Getting Started</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/java-hello-world-maven/">java-hello-world-maven</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/java-hello-world-maven.yml"><img alt="java-hello-world-maven" src="https://github.com/graalvm/graalvm-demos/actions/workflows/java-hello-world-maven.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to generate a native executable using the Native Build Tools Maven plugin <br><strong>Technologies: </strong>Native Image, Native Build Tools Maven plugin<br><strong>Reference: </strong><a href="https://docs.oracle.com/en/graalvm/jdk/21/docs/getting-started/oci/code-editor/">Oracle GraalVM in OCI Code Editor</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-image/build-java-modules/">native-image/build-java-modules</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-build-java-modules.yml"><img alt="build-java-modules" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-build-java-modules.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to build a modular Java application into a native executable<br><strong>Technologies: </strong>Native Image, Maven<br><strong>Reference: </strong><a href="https://www.graalvm.org/dev/reference-manual/native-image/guides/build-java-modules-into-native-executable/">Build Java Modules into a Native Executable</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-image/list-files/">native-image/list-files</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-list-files.yml"><img alt="native-image/list-files" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-list-files.yml/badge.svg"/></a></td>
      <td align="left" width="70%">Demonstrates how to compile a CLI application into a native executable and then apply Profile-Guided Optimizations (PGO) for more performance gains<br><strong>Technologies: </strong>Native Image, PGO
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/multithreading-demo/">multithreading-demo</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/streams.yml"><img alt="streams" src="https://github.com/graalvm/graalvm-demos/actions/workflows/streams.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to optimize a Java application that does synchronous and asynchronous threads execution<br><strong>Technologies: </strong>Native Image Build Reports, Native Build Tools Maven plugin <br><strong>Reference: </strong><a href="https://medium.com/graalvm/making-sense-of-native-image-contents-741a688dab4d">Making sense of Native Image contents</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/javagdbnative/">javagdbnative</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/javagdbnative.yml"><img alt="javagdbnative" src="https://github.com/graalvm/graalvm-demos/actions/workflows/javagdbnative.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to debug a Java application, built into a native executable in VS Code<br><strong>Technologies: </strong>Native Image, Maven, GraalVM Tools for Java<br><strong>Reference: </strong><a href="https://medium.com/graalvm/native-image-debugging-in-vs-code-2d5dda1989c1">Native Image Debugging in VS Code</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-image/add-logging/">native-image/add-logging</a><br><a href="https://github.com/graalvm/graalvm-demos/blob/ni-logging-demo/.github/workflows/native-image-add-logging.yml"><img alt="add-logging" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-add-logging.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to initialize Loggers with Native Image at the executable build or run time<br><strong>Technologies: </strong> Native Image<br><strong>Reference: </strong><a href="https://www.graalvm.org/latest/reference-manual/native-image/guides/add-logging-to-native-executable/">Add Logging to a Native Executable</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-image/add-jfr/">native-image/add-jfr</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/ni-native-executable-jfr-demo/.github/workflows/native-image-add-jfr.yml"><img alt="add-jfr" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-add-jfr.yml/badge.svg"/></a></td>
      <td align="left" width="70%">Demonstrates how to create a custom JDK Flight Recorder (JFR) event and use that in a native executable<br><strong>Technologies: </strong> Native Image, JFR, VisualVM <br><strong>Reference: </strong><a href="https://www.graalvm.org/latest/reference-manual/native-image/guides/build-and-run-native-executable-with-jfr/">Build and Run Native Executables with JFR</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-image/build-shared-library/">native-image/build-shared-library</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-build-shared-library.yml"><img alt="build-shared-library" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-build-shared-library.yml/badge.svg"/></a></td>
      <td align="left" width="70%">Demonstrates how to create a Java class library, use Native Image to create a native shared library, and then create a small C application that uses that shared library<br><strong>Technologies: </strong> Native Image, LLVM toolchain <br><strong>Reference: </strong><a href="https://www.graalvm.org/latest/reference-manual/native-image/guides/build-native-shared-library/">Build a Native Shared Library</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-image/configure-with-tracing-agent/">native-image/configure-with-tracing-agent</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-configure-with-tracing-agent.yml"><img alt="configure-with-tracing-agent" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-configure-with-tracing-agent.yml/badge.svg"/></a></td>
      <td align="left" width="70%">Demonstrates how to provide metadata for Native Image in the form of JSON configuration files using a tracing agent<br><strong>Technologies: </strong> Native Image</td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-image/build-static-images/">native-image/build-static-images</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-build-static-images.yml"><img alt="build-static-images" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-build-static-images.yml/badge.svg"/></a></td>
      <td align="left" width="70%">Demonstrates how to build a fully static and a mostly-static native executable.<br><strong>Technologies: </strong> Native Image <br><strong>Reference: </strong><a href="https://www.graalvm.org/latest/reference-manual/native-image/guides/build-static-executables/">Build a Statically Linked or Mostly-Statically Linked Native Executable</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-image/create-heap-dumps/">native-image/create-heap-dumps</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-create-heap-dumps.yml"><img alt="native-image/create-heap-dumps" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-create-heap-dumps.yml/badge.svg"/></a></td>
      <td align="left" width="70%">Demonstrates different ways to generate a heap dump from a running native executable.<br><strong>Technologies: </strong> Native Image, VisualVM <br><strong>Reference: </strong><a href="https://www.graalvm.org/latest/reference-manual/native-image/guides/create-heap-dump/">Create a Heap Dump from a Native Executable</a></td>
    </tr>  
    <tr>
      <td align="left" width="30%"><a href="/native-image/add-jmx/">native-image/add-jmx</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-add-jmx.yml"><img alt="add-jmx" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-add-jmx.yml/badge.svg"/></a></td>
      <td align="left" width="70%">This demo covers the steps required to build, run, and interact with a native executable using JMX.<br><strong>Technologies: </strong> Native Image, JMX, VisualVM <br><strong>Reference: </strong><a href="https://www.graalvm.org/dev/reference-manual/native-image/guides/build-and-run-native-executable-with-remote-jmx/">Build and Run Native Executables with Remote JMX</a></td>
    </tr>
  </tbody>
</table>

### Native Image on Cloud Platforms

<table>
  <thead>
    <tr>
      <th align="left">Name</th>
      <th align="left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="left" width="30%"><a href="/clouds/native-aws-fargate/">clouds/native-aws-fargate</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/clouds-native-aws-fargate.yml"><img alt="clouds/native-aws-fargate" src="https://github.com/graalvm/graalvm-demos/actions/workflows/clouds-native-aws-fargate.yml/badge.svg"/></a></td>
      <td align="left" width="70%">This demo covers the steps required to create a container image of a native executable application and deploy the image on AWS Fargate.<br><strong>Technologies: </strong> Native Image, Apache Maven, Docker, AWS Fargate <br>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/clouds/native-aws-lambda/">clouds/native-aws-lambda</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/clouds-native-aws-lambda.yml"><img alt="clouds/native-aws-lambda" src="https://github.com/graalvm/graalvm-demos/actions/workflows/clouds-native-aws-lambda.yml/badge.svg"/></a></td>
      <td align="left" width="70%">This demo covers the steps required to deploy a native executable application on AWS Lambda.<br><strong>Technologies: </strong> Native Image, Apache Maven, Docker, AWS Lambda <br>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/clouds/native-google-cloud-run/">clouds/native-google-cloud-run</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/clouds-native-google-cloud-run.yml"><img alt="clouds/native-google-cloud-run" src="https://github.com/graalvm/graalvm-demos/actions/workflows/clouds-native-google-cloud-run.yml/badge.svg"/></a></td>
      <td align="left" width="70%">This demo covers the steps required to create a container image of a native executable application and deploy the image on Google Cloud Run.<br><strong>Technologies: </strong> Native Image, Apache Maven, Docker, Google Cloud CLI, Google Cloud Run <br>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/clouds/native-oci-container-instances/">clouds/native-oci-container-instances</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/clouds-native-oci-container-instances.yml"><img alt="clouds/native-oci-container-instances" src="https://github.com/graalvm/graalvm-demos/actions/workflows/clouds-native-oci-container-instances.yml/badge.svg"/></a></td>
      <td align="left" width="70%">This demo covers the steps required to create a container image of a native executable application and deploy the image on OCI Container Instances.<br><strong>Technologies: </strong> Native Image, Apache Maven, Docker, OCI Container Instances<br></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/clouds/native-oci-generative-ai/">clouds/native-oci-generative-ai</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/clouds-native-oci-generative-ai.yml"><img alt="clouds/native-oci-generative-ai" src="https://github.com/graalvm/graalvm-demos/actions/workflows/clouds-native-oci-generative-ai.yml/badge.svg"/></a></td>
      <td align="left" width="70%">This demo covers the steps required to use the Generative AI service provided by Oracle Cloud Infrastructure. It uses a state-of-the-art, customizable large language model to generate text.<br><strong>Technologies: </strong> Native Image, Apache Maven, Generative AI<br></td>
    </tr>
  </tbody>
</table>

### Micronaut

<table>
  <thead>
    <tr>
      <th align="left">Name</th>
      <th align="left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="left" width="30%"><a href="/micronaut-hello-rest-maven/">micronaut-hello-rest-maven</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/micronaut-hello-rest-maven.yml"><img alt="micronaut-hello-rest-maven" src="https://github.com/graalvm/graalvm-demos/actions/workflows/micronaut-hello-rest-maven.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to package a Micronaut REST application into a native executable with Native Build Tools Maven plugin<br><strong>Technologies: </strong>Native Image, Micronaut, Native Build Tools Maven plugin<br><strong>Reference: </strong><a href="https://github.com/oracle-devrel/oci-code-editor-samples/tree/main/java-samples/graalvmee-java-micronaut-hello-rest">Try in OCI Code Editor</a></td>
    </tr>
  </tbody>
</table>

### Spring Boot

<table>
  <thead>
    <tr>
      <th align="left">Name</th>
      <th align="left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="left" width="30%"><a href="/native-image/containerize/">native-image/containerize</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-containerize.yml"><img alt="native-image/containerizee" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-image-containerize.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to compile a Spring Boot 3 application into a native executable using the Native Build Tools Maven plugin and a Maven profile <br> <strong>Technologies: </strong>Spring Boot, Native Image, Native Build Tools Maven plugin <br><strong>Reference: </strong><a href="https://www.graalvm.org/latest/reference-manual/native-image/guides/containerise-native-executable-and-run-in-docker-container/">Containerize a Native Executable and Run in a Container</a>, <a href="https://docs.oracle.com/en/graalvm/jdk/21/docs/getting-started/oci/cloud-shell/">Oracle GraalVM in OCI Cloud Shell</a></td>
    </tr>
  </tbody>
</table>

### Scala

<table>
  <thead>
    <tr>
      <th align="left">Name</th>
      <th align="left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="left" width="30%"><a href="/scalac-native/">scalac-native</a></td>
      <td align="left" width="70%">Demonstrates how to build a native executable of the Scala compiler. The resulting binary has no dependencies on the JDK. <br><strong>Technologies: </strong>Scala 2.12.x, Native Image <br><strong>Reference: </strong><a href="https://medium.com/graalvm/compiling-scala-faster-with-graalvm-86c5c0857fa3">Compiling Scala Faster with GraalVM</a></td>
    </tr>
  </tbody>
</table>

### Polyglot

<table>
  <thead>
    <tr>
      <th align="left">Name</th>
      <th align="left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="left" width="30%"><a href="/polyglot-chat-app/">polyglot-chat-app</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/polyglot-chat-app.yml"><img alt="polyglot-chat-app" src="https://github.com/graalvm/graalvm-demos/actions/workflows/polyglot-chat-app.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to build a polyglot chat application by embedding Python and R into the Java host language <br><strong>Technologies: </strong>Java, GraalPy, FastR, Micronaut</td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/polyglot-debug/">polyglot-debug</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/polyglot-debug.yml"><img alt="polyglot-debug" src="https://github.com/graalvm/graalvm-demos/actions/workflows/polyglot-debug.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to debug a polyglot Java and JavaScript application using GraalVM Tools for Java in VS Code <br><strong>Technologies: </strong>Java, JavaScript, Maven, GraalVM Extension Pack</td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/functionGraphDemo/">functionGraphDemo</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/functionGraphDemo.yml"><img alt="functionGraphDemo" src="https://github.com/graalvm/graalvm-demos/actions/workflows/functionGraphDemo.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to run a polyglot JavaScript-Java-R application on the GraalVM Node.js runtime <br><strong>Technologies: </strong>JavaScript, Node.js, Java, R</td>
    </tr>
  </tbody>
</table>

## Compatibility

The demos are normal applications and benchmarks written in Java, JavaScript, Python, etc., so they are compatible with any virtual machine capable of running Java, JavaScript and so on. 
These demos are [tested against the latest GraalVM release using GitHub Actions](https://github.com/graalvm/graalvm-demos/actions/workflows/main.yml). If you come across an issue, please submit it [here](https://github.com/graalvm/graalvm-demos/issues).

## License

Unless specified otherwise, all code in this repository is licensed under the [Universal Permissive License (UPL)](http://opensource.org/licenses/UPL).
Note that the submodule `fastR-examples` which is a reference to the [graalvm/examples](https://github.com/graalvm/examples) repository has a separate license.

## Learn More

* [GraalVM website](https://www.graalvm.org)
* [Graal project on GitHub](https://github.com/oracle/graal/tree/master/compiler)
* [GraalVM blog](https://medium.com/graalvm)
