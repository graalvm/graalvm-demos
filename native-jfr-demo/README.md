# Recording JFR events with Native Image

This demo shows how to create a custom JDK Flight Recorder (JFR) event and use it in a native executable.
JFR is a tool to collect diagnostic and profiling data in a running Java application, and built into the JVM. 
GraalVM Native Image supports JFR events and users can use the [`jdk.jfr.Event`](https://docs.oracle.com/en/java/javase/17/docs/api/jdk.jfr/jdk/jfr/Event.html) API with a similar experience to using JFR in the JVM. For example, to emit standard JFR events for the `jwebserver` application, you would run:

```bash
$JAVA_HOME/bin/native-image --enable-monitoring=jfr -m jdk.httpserver
```
```bash
./jdk.httpserver -XX:+FlightRecorder -XX:StartFlightRecording="duration=10s,filename=recording.jfr"
```

Follow the steps below to record JFR events when running a native executable, enable JFR support, and JFR recording.

> Note: JFR event recording is not yet supported on GraalVM JDK for Windows.

## Preparation

1. Download and install the latest GraalVM JDK with Native Image and VisualVM support using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) -c 'visualvm'
    ```
2. Download the demos repository or clone it as follows:
    ```bush
    git clone https://github.com/graalvm/graalvm-demos
    ```

## Build and Run Native Executables with JFR

1. Change directory to the demo subdirectory: _native-jfr-demo_:
    ```bash
    cd graalvm-demos/native-jfr-demo
    ```
2. Compile the Java file using the GraalVM JDK:
    ```bash
    $JAVA_HOME/bin/javac JFRDemo.java
    ```
    It creates two class files: _JFRDemo$HelloWorldEvent.class_ and _JFRDemo.class_.

3. Build a native executable with VM inspection enabled:
    ```bash
    $JAVA_HOME/bin/native-image --enable-monitoring=jfr JFRDemo
    ```
    The `--enable-monitoring=jfr` option enables features such as JFR that can be used to inspect the VM.

4. Run the executable and start recording:
    ```bash
    ./jfrdemo -XX:+FlightRecorder -XX:StartFlightRecording="filename=recording.jfr"
    ```
    This command runs the application as a native executable. The `-XX:+FlightRecorder` and `-XX:StartFlightRecording` options enable the built-in Flight Recorder and start recording to a specified binary file, _recording.jfr_.

5. Start [VisualVM](https://visualvm.github.io/) to view the contents of the recording file in a user-friendly way. GraalVM provides VisualVM in the core installation. To start the tool, run:
    ```bash 
    $JAVA_HOME/bin/jvisualvm
    ```

6. Go to **File**, then **Add JFR Snapshot**, browse _recording.jfr_, and open the selected file. Confirm the display name and click **OK**. Once opened, there is a bunch of options you can check: Monitoring, Threads, Exceptions, etc., but you should be mostly interested in the events browsing. It will look something like this:
    
    ![JDK Flight Recorder](img/jfr.png)

    Alternatively, you can view the contents of the recording file in the console window by running this command:
    ```shell
   $JAVA_HOME/bin/jfr print recording.jfr
    ```
    It prints all the events recorded by Flight Recorder.

### Related Documentation

- Learn more about [Native Image support for JFR events](https://www.graalvm.org/latest/reference-manual/native-image/debugging-and-diagnostics/JFR/) and how to further configure JFR recording and system logging.

- [Create and record your first event with Java](https://docs.oracle.com/en/java/javase/17/jfapi/creating-and-recording-your-first-event.html).

GraalVM Native Image supports recording events using use jdk.jfr.Event API. To build a native executable with the JFR events support, you need to add the --enable-monitoring=jfr option when invoking the native-image tool. Learn more about Native Image support for JFR events and how to further configure JFR recording and system logging.
