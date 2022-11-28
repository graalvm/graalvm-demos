# Recording JFR events with Native Image

This example demonstrates how to use the JDK Flight Recorder (JFR) as a tool to collect diagnostic and profiling data in a running Java application, built into a JVM. 
GraalVM Native Image supports JFR events and users can use the [`jdk.jfr.Event`](https://docs.oracle.com/en/java/javase/11/docs/api/jdk.jfr/jdk/jfr/Event.html) API with a similar experience to using JFR in the Java HotSpot VM.

To record JFR events when running a native executable, enable JFR support and JFR recording as described in this guide.

> Note: JFR event recording is not supported on GraalVM JDK for Windows.

## Preparation

1. Download and  install the latest GraalVM JDK with Native Image using [GraalVM JDK Dowloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk)
    ```

2. Install VisualVM by running:
    ```bash
    gu install visualvm
    ``` 

3. Download the demos repository or clone it as follows:
    ```
    git clone https://github.com/graalvm/graalvm-demos
    ```

## Build and Run Native Executables with JFR

1. Change directory to the demo subdirectory: _native-jfr-demo_:
    ```
    cd graalvm-demos/native-jfr-demo
    ```
2. Compile the Java file using the GraalVM JDK:
    ```bash
    javac JFRDemo.java
    ```
    It creates two class files: _JFRDemo$HelloWorldEvent.class_ and _JFRDemo.class_.

3. Build a native executable with VM inspection enabled:
    ```bash
    native-image --enable-monitoring=jfr JFRDemo
    ```
    The `--enable-monitoring=jfr` option enables features such as JFR that can be used to inspect the VM.

4. Run the executable and start recording:
    ```bash
    ./jfrdemo -XX:+FlightRecorder -XX:StartFlightRecording="filename=recording.jfr"
    ```
    This command runs the application as a native executable. The `-XX:StartFlightRecording` option enables the built-in Flight Recorder and starts recording to a specified binary file, _recording.jfr_.

5. Start [VisualVM](https://visualvm.github.io/) to view the contents of the recording file in a user-friendly way. GraalVM provides VisualVM in the core installation. To start the tool, run:
    ```bash 
    visualvm
    ```

6. Go to **File**, then **Add JFR Snapshot**, browse _recording.jfr_, and open       the selected file. Confirm the display name and click **OK**. Once opened, there is a bunch of options you can check: Monitoring, Threads, Exceptions, etc., but you should be mostly interested in the events browsing. It will look something like this:
    
    ![JDK Flight Recorder](img/jfr.png)

    Alternatively, you can view the contents of the recording file in the console window by running this command:
    ```shell
   jfr print recording.jfr
    ```
    It prints all the events recorded by Flight Recorder.

### Related Documentation

- Learn more about [Native Image support for JFR events](../JFR.md) and how to further configure JFR recording and system logging.

- [Create and record your first event with Java](https://docs.oracle.com/en/java/javase/17/jfapi/creating-and-recording-your-first-event.html).

GraalVM Native Image supports recording events using use jdk.jfr.Event API. To build a native executable with the JFR events support, you need to add the --enable-monitoring=jfr option when invoking the native-image tool. Learn more about Native Image support for JFR events and how to further configure JFR recording and system logging.
