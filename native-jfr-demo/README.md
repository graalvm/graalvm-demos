# Recording JFR events with Native Image

This demo shows how to create a custom JDK Flight Recorder (JFR) event and use it in a native executable.
JFR is a tool, built into the JVM, to collect diagnostic and profiling data in a running Java application.
GraalVM Native Image supports JFR events and users can use the [`jdk.jfr.Event`](https://docs.oracle.com/en/java/javase/21/docs/api/jdk.jfr/jdk/jfr/Event.html) API with a similar experience to using JFR in the JVM. 

Follow the steps below to record JFR events when running a native executable, enable JFR support, and JFR recording.

> Note: JFR event recording is not yet supported on GraalVM JDK for Windows.

## Preparation

1. Download and install the latest GraalVM JDK using [SDKMAN!](https://sdkman.io/).
    ```bash
    sdk install java 21.0.1-graal
    ```
2. Download the demos repository or clone it as follows:
    ```bush
    git clone https://github.com/graalvm/graalvm-demos
    ```
3. Navigate into the _native-jfr-demo_ directory:
    ```bash
    cd graalvm-demos/native-jfr-demo
    ```

## Build and Run a Native Executable with JFR

1. Compile the Java file using the GraalVM JDK:
    ```bash
    $JAVA_HOME/bin/javac JFRDemo.java
    ```
    It creates two class files: _JFRDemo$HelloWorldEvent.class_ and _JFRDemo.class_.

3. Build a native executable with VM inspection enabled:
    ```bash
    $JAVA_HOME/bin/native-image --enable-monitoring=jfr JFRDemo
    ```
    The `--enable-monitoring=jfr` option enables features such as JFR that can be used to inspect the VM.

4. Run the executable, emitting standard JFR events, and start recording:
    ```bash
    ./jfrdemo -XX:+FlightRecorder -XX:StartFlightRecording="filename=recording.jfr"
    ```
    This command runs the application as a native executable. The `-XX:+FlightRecorder` and `-XX:StartFlightRecording` options enable the built-in Flight Recorder and start recording to a specified binary file, _recording.jfr_.

5. Start [VisualVM](https://visualvm.github.io/) to view the contents of the recording file in a user-friendly way.

6. Go to **File**, then **Add JFR Snapshot**, browse _recording.jfr_, and open the selected file. Confirm the display name and click **OK**. Once opened, there is a bunch of options you can check: Monitoring, Threads, Exceptions, etc., but you should be mostly interested in the events browsing. It will look something like this:
    
    ![JDK Flight Recorder](img/jfr.png)

    Alternatively, you can view the contents of the recording file in the console window by running this command:
    ```shell
    $JAVA_HOME/bin/jfr print recording.jfr
    ```
    It prints all the events recorded by Flight Recorder.

### Related Documentation

- Learn more about [JDK Flight Recorder (JFR) with Native Image](https://www.graalvm.org/latest/reference-manual/native-image/debugging-and-diagnostics/JFR/) and how to further configure JFR recording and system logging.

- [Create and record your first event with Java](https://docs.oracle.com/en/java/javase/17/jfapi/creating-and-recording-your-first-event.html).