# Native Executables with JFR Example

This example demonstrates how JDK Flight Recorder (JFR) as a tool for collecting diagnostic and profiling data running Java application, built into a JVM. 
GraalVM Native Image supports JFR events and users can use [`jdk.jfr.Event`](https://docs.oracle.com/en/java/javase/11/docs/api/jdk.jfr/jdk/jfr/Event.html) API with a similar experience to using JFR in the Java HotSpot VM.

To record JFR events when running a native executable, JFR support and JFR recording must be enabled, and this guide covers how to do that.

> Note: JFR events recording is not supported on GraalVM JDK for Windows. JFR is only supported with native executables built on GraalVM JDK 11.

## Preparation

1. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, set the GraalVM home directory as the `$JAVA_HOME` environment variable and add `$JAVA_HOME/bin` to the `PATH` environment variable.

    On Linux:
    ```bash
    export JAVA_HOME=/home/${current_user}/path/to/graalvm
    export PATH=$JAVA_HOME/bin:$PATH
    ```
    On macOS:
    ```bash
    export JAVA_HOME=/Users/${current_user}/path/to/graalvm/Contents/Home
    export PATH=$JAVA_HOME/bin:$PATH
    ```

2. Install [Native Image](https://www.graalvm.org/dev/reference-manual/native-image/#install-native-image) and VisualVM by running:
    ```bash
    gu install native-image jvisualvm
    ``` 

3. Download or clone the demos repository:
    ```
    git clone https://github.com/graalvm/graalvm-demos
    ```

## Build and Run Native Executables with JFR

1. Change directory to the demo subdirectories: _native-jfr-demo_:
    ```
    cd graalvm-demos/native-jfr-demo
    ```
2. Compile the Java file on GraalVM JDK:
    ```bash
    javac JFRDemo.java
    ```
    It creates two class files: _JFRDemo$HelloWorldEvent.class_ and _JFRDemo.class_.

3. Build a native executable with the VM inspection enabled:
    ```bash
    native-image --enable-monitoring=jfr JFRDemo
    ```
    The `--enable-monitoring=jfr` option enables features such as JFR that can be used to inspect the VM.

4. Run the executable and start recording:
    ```bash
    ./jfrdemo -XX:+FlightRecorder -XX:StartFlightRecording="filename=recording.jfr"
    ```
    It runs the application as a native executable. The `-XX:StartFlightRecording` option enables the built-in Flight Recorder and starts recording to a specified binary file, _recording.jfr_.

5. Start [VisualVM](https://visualvm.github.io/) to view the contents of the recording file in a user-friendly way. GraalVM provides VisualVM in the core installation. To start the tool, run:
    ```bash 
    jvisualvm
    ```

6. Go to **File**, then **Add JFR Snapshot**, browse _recording.jfr_, and open       the selected file. Confirm the display name and click **OK**. Once opened, there is a bunch of options you can check: Monitoring, Threads, Exceptions, etc., but you should be mostly interested in the events browsing. It will look something like this:
    
    ![JDK Flight Recorder](img/jfr.png)

    Alternatively, you can view the contents of the recording file in the console window by running this command:
    ```shell
   jfr print recording.jfr
    ```
    
    It prints all events recorded by Flight Recorder.
