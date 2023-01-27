# Creating a Heap Dump from a Native Executable

You can create a heap dump of a running executable to monitor its execution. Just like any other Java heap dump, it can be opened with the [VisualVM](https://www.graalvm.org/latest/tools/visualvm/) tool. To enable heap dump support, native executables must be built with the `--enable-monitoring=heapdump` option.
Then you can request heap dumps in the same way you can request them when your application runs on the JVM (for example, right-click on the process, then select "Heap Dump"). 

There are different ways to create heap dumps:
* Create heap dumps with VisualVM.
* Dump the initial heap of a native executable using the `-XX:+DumpHeapAndExit` command-line option.
* Create heap dumps sending a `SIGUSR1` signal at run time.
* Create heap dumps programmatically using the [`org.graalvm.nativeimage.VMRuntime#dumpHeap`](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/VMInspectionOptions.java) API.

All approaches are described below.

>Note: Creating heap dumps is not available on the Microsoft Windows platform.

## Preparation

1. Download and install the latest GraalVM JDK with Native Image and VisualVM support using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader):
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) -c 'visualvm'
    ```

2. Download the demos repository or clone it as follows:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    ```bash
    cd graalvm-demos/native-heapdump-examples
    ```

## Dump the Initial Heap of a Native Executable

Use the `-XX:+DumpHeapAndExit` command-line option to dump the initial heap of a native executable.
This can be useful to identify which objects the `native-image` build process allocated to the executable's heap. 
For a HelloWorld example, use the option as follows:

```shell
$JAVA_HOME/bin/native-image HelloWorld --enable-monitoring=heapdump
```
```shell
./helloworld -XX:+DumpHeapAndExit
```
The heap dump is created in the application's working directory, `/path/to/helloworld.hprof`.

## Create Heap Dumps with SIGUSR1 (Linux/macOS only)

>Note: This requires the `Signal` API, which is enabled by default except when building shared libraries.

The following example is a simple multi-threaded Java application that runs for 60 seconds. 
This provides you with enough time to send it a `SIGUSR1` signal. The application will handle the signal and create a heap dump in the application's working directory. The heap dump will contain the `Collection` of `Person`s referenced by the static variable `CROWD`.

Follow these steps to build a native executable that will produce a heap dump when it receives a `SIGUSR1` signal.

### 1. Build the Application

1. Compile _SVMHeapDump.java_ using `javac`:
    ```bash
    $JAVA_HOME/bin/javac SVMHeapDump.java
    ```

2. Build a native executable using the `--enable-monitoring=heapdump` command-line option:
    ```shell
    $JAVA_HOME/bin/native-image SVMHeapDump --enable-monitoring=heapdump
    ```
    This causes the resulting native executable to produce a heap dump when it receives a `SIGUSR1` signal.

    The `native-image` builder creates a native executable from the `SVMHeapDump.class`.
    When the command completes, the native executable `svmheapdump` is created in the current directory.

### 2. Run the Application

1.  Run the application, send it a signal, and check the heap dump:
    ```shell
    ./svmheapdump
    ```
    ```
    17 May 2022, 16:38:13: Hello GraalVM native image developer! 
    The PID of this process is: 57509
    Send it a signal: 'kill -SIGUSR1 57509' 
    to dump the heap into the working directory.
    Starting thread!
    17 May 2022, 16:38:13: Thread started, it will run for 60 seconds
    ```

2. Make a note of the PID and open a second terminal. Use the PID to send a signal to the application. For example, if the PID is `57509`:
    ```bash
    kill -SIGUSR1 57509
    ```
    The heap dump will be created in the working directory while the application continues to run. The heap dump can be opened with the [VisualVM](https://www.graalvm.org/latest/tools/visualvm/) tool, as illustrated below.

    ![Native Image Heap Dump View in VisualVM](img/heap-dump.png)

## Create a Heap Dump from within a Native Executable

The following example shows how to create a heap dump from a running native executable using [`VMRuntime.dumpHeap()`](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/VMInspection.java) if some condition is met.
The condition to create a heap dump is provided as an option on the command line.
1. Compile _SVMHeapDumpAPI.java_:
    ```shell
    $JAVA_HOME/bin/javac SVMHeapDumpAPI.java
    ```
    As in the earlier example, the application creates a `Collection` of `Person`s referenced by the static variable `CROWD`. It then checks the command line to see if a heap dump has to be created, then the method `createHeapDump()` creates the heap dump.

2. Build a native executable:
    ```bash
    $JAVA_HOME/bin/native-image SVMHeapDumpAPI
    ```
    When the command completes, the `svmheapdumpapi` native executable is created in the current directory.

3. Now run your native executable and create a heap dump from it with output similar to the following:

    ```shell
    ./svmheapdumpapi --heapdump
    ```
    ```
    Sep 15, 2020, 4:06:36 PM: Hello GraalVM native image developer.
    Your command line options are: --heapdump
    Heap dump created /var/folders/hw/s9d78jts67gdc8cfyq5fjcdm0000gp/T/SVMHeapDump-6437252222863577987.hprof, size: 8051959
    ```
    The resulting heap dump can be then opened with the [VisualVM](https://www.graalvm.org/latest/tools/visualvm/) tool like any other Java heap dump, as illustrated below.

    ![Native Image Heap Dump View in VisualVM](img/heap-dump-api.png)

>Note: By default, heap dumps are created in the current working directory. The `-XX:HeapDumpPath` option can be used to specify an alternative filename or directory. For example:  
> `./helloworld -XX:HeapDumpPath=$HOME/helloworld.hprof`

### Related Documentation

* [Debugging and Diagnostics](https://www.graalvm.org/latest/reference-manual/native-image/debugging-and-diagnostics/)
* [VisualVM](https://www.graalvm.org/latest/tools/visualvm/)