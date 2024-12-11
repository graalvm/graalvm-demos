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
      <td align="left" width="30%"><a href="/hello-graal/">hello-graal</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/hello-graal.yml"><img alt="hello-graal" src="https://github.com/graalvm/graalvm-demos/actions/workflows/hello-graal.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to build native executables from a class file and a JAR file from the command line <br><strong>Technologies: </strong> Native Image <br><strong>Reference: </strong><a href="https://www.graalvm.org/dev/reference-manual/native-image/#build-a-native-executable">Native Image Getting Started</a></td>
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
      <td align="left" width="30%"><a href="/native-list-dir/">native-list-dir</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-list-dir.yml"><img alt="native-list-dir" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-list-dir.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to compile a CLI application into a native executable and then apply Profile-Guided Optimizations (PGO) for more performance gains<br><strong>Technologies: </strong>Native Image, PGO
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/java-simple-stream-benchmark/">java-simple-stream-benchmark</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/java-simple-stream-benchmark.yml"><img alt="java-simple-stream-benchmark" src="https://github.com/graalvm/graalvm-demos/actions/workflows/java-simple-stream-benchmark.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how the Graal compiler can achieve better performance for highly abstracted programs like those using Streams, Lambdas<br><strong>Technologies: </strong>Graal compiler, C2<br><strong>Reference: </strong><a href="https://luna.oracle.com/lab/d502417b-df66-45be-9fed-a3ac8e3f09b1/steps#task-2-run-demos-java-microbenchmark-harness-jmh">Simple Java Stream Benchmark</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/streams/">streams</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/streams.yml"><img alt="streams" src="https://github.com/graalvm/graalvm-demos/actions/workflows/streams.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how GraalVM efficiently optimizes the Java Streams API application and how to apply PGO<br><strong>Technologies: </strong>Native Image, Native Build Tools Maven Plugin <br><strong>Reference: </strong><a href="https://www.graalvm.org/latest/reference-manual/native-image/guides/optimize-native-executable-with-pgo/">Optimize a Native Executable with Profile-Guided Optimizations</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/fortune-demo/">fortune-demo</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/fortune-demo.yml"><img alt="fortune-demo" src="https://github.com/graalvm/graalvm-demos/actions/workflows/fortune-demo.yml/badge.svg" /></a></td>
      <td align="left" width="70%">A fortune teller Unix program. Build a native executable using Gradle or Maven Native Image plugins.<br><strong>Technologies: </strong>Native Image, Native Build Tools Maven plugin <br><strong>Reference: </strong><a href="https://graalvm.github.io/native-build-tools/latest/maven-plugin-quickstart.html">Getting Started with Maven Plugin for GraalVM Native Image</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/multithreading-demo/">multithreading-demo</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/streams.yml"><img alt="streams" src="https://github.com/graalvm/graalvm-demos/actions/workflows/streams.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to optimize a Java application that does synchronous and asynchronous threads execution<br><strong>Technologies: </strong>Native Image Build Reports, Native Build Tools Maven plugin <br><strong>Reference: </strong><a href="https://medium.com/graalvm/making-sense-of-native-image-contents-741a688dab4d">Making sense of Native Image contents</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-image-configure-examples/">native-image-configure-examples</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/streams.yml"><img alt="streams" src="https://github.com/graalvm/graalvm-demos/actions/workflows/streams.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how you can influence the classes initialization at the image build time<br><strong>Technologies: </strong>Native Image, Maven<br><strong>Reference: </strong><a href="https://medium.com/graalvm/understanding-class-initialization-in-graalvm-native-image-generation-d765b7e4d6ed">Understanding Class Initialization in GraalVM Native Image Generation</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-netty-plot/">native-netty-plot</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-netty-plot.yml"><img alt="native-netty-plot" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-netty-plot.yml/badge.svg" /></a></td>
      <td align="left" width="70%">A web server application, using the Netty framework, to demonstrate the use of isolates with Native Image<br><strong>Technologies: </strong>Native Image, Maven, Netty<br><strong>Reference: </strong><a href="https://medium.com/graalvm/instant-netty-startup-using-graalvm-native-image-generation-ed6f14ff7692">Instant Netty Startup using GraalVM Native Image Generation</a></td>
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
      <td align="left" width="30%"><a href="/native-heapdump-examples/">native-heapdump-examples</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-heapdump-examples.yml"><img alt="native-heapdump-examples" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-heapdump-examples.yml/badge.svg"/></a></td>
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
      <td align="left" width="30%"><a href="/native-aws-fargate/">native-aws-fargate</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-aws-fargate.yml"><img alt="native-aws-fargate" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-aws-fargate.yml/badge.svg"/></a></td>
      <td align="left" width="70%">This demo covers the steps required to create a container image of a native executable application and deploy the image on AWS Fargate.<br><strong>Technologies: </strong> Native Image, Apache Maven, Docker, AWS Fargate <br>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-aws-lambda/">native-aws-lambda</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-aws-lambda.yml"><img alt="native-aws-lambda" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-aws-lambda.yml/badge.svg"/></a></td>
      <td align="left" width="70%">This demo covers the steps required to deploy a native executable application on AWS Lambda.<br><strong>Technologies: </strong> Native Image, Apache Maven, Docker, AWS Lambda <br>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-google-cloud-run/">native-google-cloud-run</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-google-cloud-run.yml"><img alt="native-google-cloud-run" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-google-cloud-run.yml/badge.svg"/></a></td>
      <td align="left" width="70%">This demo covers the steps required to create a container image of a native executable application and deploy the image on Google Cloud Run.<br><strong>Technologies: </strong> Native Image, Apache Maven, Docker, Google Cloud CLI, Google Cloud Run <br>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-oci-container-instances/">native-oci-container-instances</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-oci-container-instances.yml"><img alt="native-oci-container-instances" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-oci-container-instances.yml/badge.svg" /></a></td>
      <td align="left" width="70%">This demo covers the steps required to create a container image of a native executable application and deploy the image on OCI Container Instances.<br><strong>Technologies: </strong> Native Image, Apache Maven, Docker, OCI Container Instances<br></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/native-oci-generative-ai/">native-oci-generative-ai</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/native-oci-generative-ai.yml"><img alt="native-oci-generative-ai" src="https://github.com/graalvm/graalvm-demos/actions/workflows/native-oci-generative-ai.yml/badge.svg" /></a></td>
      <td align="left" width="70%">This demo covers the steps required to use the Generative AI service provided by Oracle Cloud Infrastructure. It uses a state-of-the-art, customizable large language model to generate text.<br><strong>Technologies: </strong> Native Image, Apache Maven, Generative AI<br></td>
    </tr>
  </tbody>
</table>

### Java on Truffle (Espresso)

<table>
  <thead>
    <tr>
      <th align="left">Name</th>
      <th align="left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="left" width="30%"><a href="/espresso-jshell/">espresso-jshell</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/espresso-jshell.yml"><img alt="espresso-jshell" src="https://github.com/graalvm/graalvm-demos/actions/workflows/espresso-jshell.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to build a native executable of JShell, that executes the dynamically generated bytecode on Espresso<br><strong>Technologies: </strong>Java on Truffle, Native Image, JShell<br><strong>Reference: </strong><a href="https://www.graalvm.org/dev/reference-manual/java-on-truffle/demos/#mixing-aot-and-jit-for-java">Mixing AOT and JIT for Java</a>, <a href="https://medium.com/graalvm/java-on-truffle-going-fully-metacircular-215531e3f840">Java on Truffle — Going Fully Metacircular</a></td>
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
      <td align="left" width="30%"><a href="/spring-native-image/">spring-native-image</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/spring-native-image.yml"><img alt="spring-native-image" src="https://github.com/graalvm/graalvm-demos/actions/workflows/spring-native-image.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates how to compile a Spring Boot application into a native executable using the Native Build Tools Maven plugin and a Maven profile <br> <strong>Technologies: </strong>Spring Boot, Native Image, Native Build Tools Maven plugin <br><strong>Reference: </strong><a href="https://luna.oracle.com/lab/fdfd090d-e52c-4481-a8de-dccecdca7d68/steps">GraalVM Native Image, Spring and Containerisation</a>, <a href="https://docs.oracle.com/en/graalvm/jdk/21/docs/getting-started/oci/cloud-shell/">Oracle GraalVM in OCI Cloud Shell</a></td>
    </tr>
    <tr>
      <td align="left" width="30%"><a href="/spring-r/">spring-r</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/spring-r.yml"><img alt="spring-r" src="https://github.com/graalvm/graalvm-demos/actions/workflows/spring-r.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates GraalVM's polyglot feature by loading an R script into a Java host application 
      <br><strong>Technologies: </strong> Spring, FastR <br><strong>Reference: </strong><a href="https://medium.com/graalvm/enhance-your-java-spring-application-with-r-data-science-b669a8c28bea">Enhance your Java Spring application with R data science</a></td>
    </tr>
  </tbody>
</table>

### Helidon

<table>
  <thead>
    <tr>
      <th align="left">Name</th>
      <th align="left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="left" width="30%"><a href="/js-java-async-helidon/">js-java-async-helidon</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/js-java-async-helidon.yml"><img alt="js-java-async-helidon" src="https://github.com/graalvm/graalvm-demos/actions/workflows/js-java-async-helidon.yml/badge.svg" /></a></td>
      <td align="left" width="70%">An HTTP web service that demonstrates how multiple JavaScript contexts can be executed in parallel to handle asynchronous operations with Helidon in Java <br><strong>Technologies: </strong>Native Image, Helidon, Native Build Tools Maven plugin <br><strong>Reference: </strong><a href="https://medium.com/graalvm/asynchronous-polyglot-programming-in-graalvm-javascript-and-java-2c62eb02acf0">Asynchronous Polyglot Programming in GraalVM Using Helidon and JavaScript</a></td>
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
      <td align="left" width="30%"><a href="/polyglot-javascript-java-r/">polyglot-javascript-java-r</a><br><a href="https://github.com/graalvm/graalvm-demos/actions/workflows/polyglot-javascript-java-r.yml"><img alt="polyglot-javascript-java-r" src="https://github.com/graalvm/graalvm-demos/actions/workflows/polyglot-javascript-java-r.yml/badge.svg" /></a></td>
      <td align="left" width="70%">Demonstrates the polyglot capabilities of GraalVM and how to run a JavaScript-Java-R application <br><strong>Technologies: </strong>JavaScript, Node.js, Java, R <br><strong>Reference: </strong><a href="https://medium.com/graalvm/graalvm-ten-things-12d9111f307d#656f">Top 10 Things To Do With GraalVM</a></td>
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
