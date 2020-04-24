# Application Initialization at Build Time

This example demonstrates the ability of native-image to run parts of your application at image-build time.

In both examples we use the Jackson framework to parse a JSON file to determine
which `Handler` should be used by the application (`CurrentTimeHandler` or
`HelloWorldHandler`) at runtime.

* In `configure-at-runtime-example` the JSON parsing happens at image runtime
  and thus contributes to the image startup time. In addition, all methods and
  static fields originated from the Jackson framework that are needed become part
  of a native-image.

* In contrast `configure-at-buildtime-example` performs the JSON parsing as
  part of building the image. In this case the image does not contain any parts
  of the Jackson framework (can be verified easily by adding
  `-H:+PrintAnalysisCallTree` to the `<buildArgs>` in `pom.xml`).  When this
  image gets executed it can run the handler right away since it was already
  determined at build time which hander should be used.

To learn more about this topic please read [Initialize Once, Start Fast: Application Initializationat Build Time](http://www.christianwimmer.at/Publications/Wimmer19a/Wimmer19a.pdf).

# Use following instructions to build the examples:

* Download [GraalVM](https://www.graalvm.org/downloads).
* Unzip the archive and set `JAVA_HOME` to the GraalVM home directory.
* Install [Native Image](https://www.graalvm.org/docs/reference-manual/native-image/#install-native-image). For GraalVM Community users, run `$JAVA_HOME/bin/gu install native-image`.
For GraalVM Enterprise users, download the Native Image JAR file from [Oracle Technology Network](https://www.oracle.com/downloads/graalvm-downloads.html) and install it by running `$JAVA_HOME/bin/gu -L install component.jar`, where -L option, equivalent to --local-file or --file, tells to install a component from a downloaded archive.
* Change to the example subdirectories and run `mvn package` there.
* Once you are done with building both images run:
  * `$JAVA_HOME/bin/native-image --server-shutdown`
* The built executables are:
  * `configure-at-runtime-example/target/example`
  * `configure-at-buildtime-example/target/example`
