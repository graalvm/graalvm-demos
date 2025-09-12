# GraalVM Native Image: Using the `-H:Preserve` Option

This demo shows how to use GraalVM Native Image’s experimental -H:Preserve
option to ensure classes accessed via reflection are included in your native
executable. You’ll learn how to identify missing classes, use build reports, and
apply the new option for reliable runtime behavior.

## Introduction

Java reflection lets a running program inspect and invoke classes, fields, and
methods at runtime. GraalVM Native Image supports many common cases
automatically by performing static analysis to discover what must be included in
the native executable. However, elements that are only accessed reflectively may
not be detected and therefore can be missing at runtime unless you declare them.

You can declare reflective access either via metadata (in code or JSON) or,
starting in GraalVM 25, via the experimental `-H:Preserve` option. This demo
shows how to use `-H:Preserve` to keep specific packages available at runtime.

## Prerequisites

- GraalVM 25
- Maven 3.9+
- SDKMAN! (recommended for installing GraalVM)

Install GraalVM 25 with SDKMAN!:

```bash
sdk install java 25-graal
sdk use java 25-graal
```

Clone the example repository:

```bash
git clone https://github.com/graalvm/graalvm-demos
cd graalvm-demos/native-image/preserve-package
```

## How the Example Works

`ReflectionExample` uses command-line arguments to reflectively:
- load a class by name,
- find a method,
- and invoke it with a string argument.

The core code is:

```java
Class<?> clazz = Class.forName(className);
Method method = clazz.getDeclaredMethod(methodName, String.class);
Object result = method.invoke(null, input);
```

Two sample actions are provided:
- `org.graalvm.example.action.StringReverser#reverse(String)`
- `org.graalvm.example.action.StringCapitalizer#capitalize(String)`

This works on the JVM as long as those classes are on the classpath.

### Run on the JVM

1. Build the JAR:

```bash
./mvnw package
```

2. Invoke `StringReverser`:

```bash
java -jar target/preserve-package-1.0-SNAPSHOT.jar \
  org.graalvm.example.action.StringReverser reverse "hello"
```

Expected output:

```
olleh
```

3. Invoke `StringCapitalizer`:

```bash
java -jar target/preserve-package-1.0-SNAPSHOT.jar \
  org.graalvm.example.action.StringCapitalizer capitalize "hello"
```

Expected output:

```
HELLO
```

## Build a Native Image

The project uses the Native Build Tools Maven plugin to drive `native-image`.
The `native-default` profile produces an executable without additional
reflection configuration.

1. Build:

```bash
./mvnw package -Pnative-default
```

2. Run:

```bash
./target/example-default \
  org.graalvm.example.action.StringReverser reverse "hello"
```

You should see a `ClassNotFoundException` similar to:

```
Exception in thread "main" java.lang.ClassNotFoundException: org.graalvm.example.action.StringReverser
  at org.graalvm.nativeimage.builder/com.oracle.svm.core.hub.ClassForNameSupport.forName(ClassForNameSupport.java:339)
  ...
  at org.graalvm.example.ReflectionExample.main(ReflectionExample.java:56)
```

This happens because static analysis did not discover that `StringReverser` (and
`StringCapitalizer`) are used via reflection, so they were not included.

## Identify Dynamic Access With the Build Report

GraalVM 25 adds an experimental reporting option to help you find dynamic access
before it breaks at runtime. With `-H:+ReportDynamicAccess`, in conjunction with
`--emit=build-report`, the Native Image [build
report](https://www.graalvm.org/latest/reference-manual/native-image/overview/build-report/)
highlights reflective usage present in the image.

The `native-default` profile already enables this feature:

```xml
<buildArgs>
  <buildArg>-H:+UnlockExperimentalVMOptions</buildArg>
  <buildArg>-H:+ReportDynamicAccess</buildArg>
  <buildArg>--emit=build-report</buildArg>
</buildArgs>
```

After building, open the report, `target/example-default-build-report.html`, and navigate to the “Dynamic Access” tab to review reflection usage in `ReflectionExample#main`.


The report highlights code that needs to be reviewed to ensure successful
runtime execution of the application. In this application, the classes loaded
via `Class.forName(...)` need to be included in the executable.

![Native Image Build Report - Dynamic Access](build-report.jpeg)

## Fix It With ‘-H:Preserve’ (Experimental)

GraalVM 25 introduces the experimental `-H:Preserve` option to keep entire
packages, modules, or all classes on the classpath.

In this project, both action classes are in the `org.graalvm.example.action`
package so `package=org.graalvm.example.action` can be added to the
`-H:Preserve` option.  The Maven `native-preserve` profile adds the necessary
`native-image` command line args:

```xml
<buildArgs>
  <buildArg>-H:+UnlockExperimentalVMOptions</buildArg>
  <buildArg>-H:Preserve=package=org.graalvm.example.action</buildArg>
</buildArgs>
```

1. Build with preserve:

```bash
./mvnw package -Pnative-preserve
```

2. Run `StringReverser`:

```bash
./target/example-preserve \
  org.graalvm.example.action.StringReverser reverse "hello"
```

Expected output:

```
olleh
```

3. Run `StringCapitalizer`:

```bash
./target/example-preserve \
  org.graalvm.example.action.StringCapitalizer capitalize "hello"
```

Expected output:

```
HELLO
```

As shown:
- `-H:+ReportDynamicAccess` helps identify code paths involving reflection.
- `-H:Preserve` makes it straightforward to include the required classes when
  static analysis alone cannot find them.

## Tips

- Always include `-H:+UnlockExperimentalVMOptions` when using experimental
  options like `-H:Preserve` or `-H:+ReportDynamicAccess`.
- If you maintain larger apps, start with the dynamic access report to scope
  what needs preserving, then apply `-H:Preserve` to the smallest package(s)
  that cover your use cases.
- For fine-grained control or for libraries you don’t own, consider JSON
  reachability metadata as a complement or alternative.

## Related Documentation

- Reachability Metadata (Reflection):
  https://www.graalvm.org/latest/reference-manual/native-image/metadata/
- Assisted Configuration with Tracing Agent:
  https://www.graalvm.org/latest/reference-manual/native-image/metadata/AutomaticMetadataCollection/#tracing-agent
- Java Reflection API (JDK 25):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/reflect/package-summary.html
