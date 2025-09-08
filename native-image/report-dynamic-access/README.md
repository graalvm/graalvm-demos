# Using Native Image `ReportDynamicAccess` Option

GraalVM Native Image performs static analysis to determine which classes, methods, and fields are needed at runtime. However, some dynamic features (such as reflection, resource loading, or JNI) may not be detected automatically. The `ReportDynamicAccess` option helps you identify such dynamic calls in your application by including them in the build report.

This guide shows how to use the `ReportDynamicAccess` option with GraalVM Native Image to generate a build report that highlights dynamic access in your application.

## Preparation

1. Download and install the latest GraalVM for JDK 25 (or the early access build before 2025-09-16) using [SDKMAN!](https://sdkman.io/).

    ```shell
    sdk install java 25.ea.29-graal
    ```

2. Download or clone this repository, and navigate into the `native-image/report-dynamic-access` directory:

    ```shell
    git clone https://github.com/graalvm/graalvm-demos
    cd graalvm-demos/native-image/report-dynamic-access
    ```

## Example: Using `ReportDynamicAccess` with DynamicHello

This directory includes a simple example, `DynamicHello.java`, which uses reflection to create and manipulate a `StringBuilder` at runtime. This will trigger dynamic access reporting in the build report.

**Note:**

- Ensure `DynamicHello.java` begins with the line: `package org.graalvm.example;`
- The file should be located at `src/main/java/org/graalvm/example/DynamicHello.java`.

### 1. Compiling the Example

From the root of this directory, compile the Java file and output classes to the current directory (so the package structure is preserved):

```shell
javac -d . src/main/java/org/graalvm/example/DynamicHello.java
```

### 2. Packaging as a Runnable JAR

From the same directory (where the `org/` folder was created by `javac -d .`), create a runnable JAR file:

```shell
jar --create --file dynamic-hello.jar --main-class=org.graalvm.example.DynamicHello org/graalvm/example/DynamicHello.class
```

### 3. Building a Native Executable with Dynamic Access Reporting

Run the following command to build a native executable and generate a build report:

```shell
native-image -H:+UnlockExperimentalVMOptions -H:+ReportDynamicAccess --emit=build-report -jar dynamic-hello.jar
```

This will produce a native executable and a build report HTML file. The report is usually named `build-report.html` or similar, and may be in the current directory or a subdirectory (such as `build/`, `reports/`, or `META-INF/native-image/`). If you do not see the file, search for `*.html` in your project directory.

### 4. Reviewing the Build Report

After the build completes, open the generated build report HTML file in your browser.

Navigate to the **Dynamic Access** tab to see:

- All detected dynamic calls requiring metadata (such as the reflection in `DynamicHello`).
- Entries that may need further investigation.
- Links to configuration files and JARs, if available.

## Understanding the Build Report

- If no dynamic calls are detected for a class or module path entry, no further action is needed.
- If the entry includes `native-image.properties` or `reachability-metadata.json`, or these files are provided externally, no further investigation is required.
- If integrated configuration or external metadata (for example, `reflect-config.json`) exists for each detected call type, no further investigation is required.
- If none of the above apply, the entry may require further investigation.

For each entry with detected dynamic calls, you can expand the entry in the report to see the specific methods and their call locations. The report also provides links to configuration files, whether they are packaged in JARs or available in directories.

> Only dynamic calls found in reachable code are reported. Some entries may have existing metadata but no reported dynamic calls.

## Troubleshooting

- **Error: Could not find or load main class ...**
  - Make sure your `DynamicHello.java` file has the correct `package` declaration and is compiled with `javac -d . ...`.
  - Ensure you create the JAR from the root of the compiled classes, preserving the package structure.

- **Build report not found:**
  - The report may be in a subdirectory or have a different name. Search for `*.html` after building.

- **Native image build fails:**
  - Check that your GraalVM version supports the required options and that your JAR runs with `java -jar` before building the native image.

### Related Documentation

- [Build Output and Build Report](https://www.graalvm.org/latest/reference-manual/native-image/overview/Options/#build-output-and-build-report)
- [Native Image Build Output](https://www.graalvm.org/latest/reference-manual/native-image/overview/BuildOutput/)
