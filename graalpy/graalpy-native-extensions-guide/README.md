# Using Python Native Extension in a Java SE Application

*Warning:
GraalPy support for native [Python extensions](https://packaging.python.org/en/latest/guides/packaging-binary-extensions) is experimental.*

Python libraries that incorporate native Python extensions, such as *NumPy* or *scikit-learn*,
can be used and shipped with Java applications.

The [GraalPy Maven artifacts](https://central.sonatype.com/artifact/org.graalvm.polyglot/python) and [GraalVM Polyglot APIs](https://www.graalvm.org/latest/reference-manual/embed-languages/) allow flexible integration with different project setups.

Using Python packages in Java projects often requires a bit more setup, due to the nature of the Python packaging ecosystem.
GraalPy provides a [python-embedding](https://central.sonatype.com/artifact/org.graalvm.python/python-embedding) package that simplifies the required setup to ship Python packages as Java resources or in separate folders.
The important entry points to do so are the [VirtualFileSystem](https://github.com/oracle/graalpython/blob/master/docs/user/Embedding-Build-Tools.md#virtual-filesystem) and the [GraalPyResources](https://github.com/oracle/graalpython/blob/master/docs/user/Embedding-Build-Tools.md#deployment) classes.

## 1. Getting Started

In this guide, we will use Python package [polyleven](https://pypi.org/project/polyleven),
which is a native Python extension, to calculate the [Levenshtein distance](https://en.wikipedia.org/wiki/Levenshtein_distance).

We will develop a CLI app that once for all settles the common source of contention: whether something is a fruit.
For each command line argument, the app will print an indication whether it is a fruit or not.
We want to guard our users against typos, so if the argument appears to be mistyped name of a fruit,
we add the standard "did you mean 'xyz'" message. For identifying the typos we will use the
Levenshtein distance.

## 2. What you will need

To complete this guide, you will need the following:

* Some time on your hands
* A decent text editor or IDE
* A supported JDK[^1], preferably the latest [GraalVM JDK](https://graalvm.org/downloads/)
* C compiler toolchain (e.g., GCC)

[^1]: Oracle JDK 17 and OpenJDK 17 are supported with interpreter only.
GraalVM JDK 21, Oracle JDK 21, OpenJDK 21 and newer with [JIT compilation](https://www.graalvm.org/latest/reference-manual/embed-languages/#runtime-optimization-support).
Note: GraalVM for JDK 17 is **not supported**.

## 3. Solution

We recommend that you follow the instructions in the next sections and create the application step by step.
However, you can go right to the [completed example](https://github.com/graalvm/graalpy-demos/tree/master/nativext).

## 4. Writing the application

You can start with any Maven application that runs on JDK 17 or newer.
We will use a default Maven application [generated](https://maven.apache.org/archetypes/maven-archetype-quickstart/) from an archetype.

```shell
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes \
  -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 \
  -DgroupId=example -DartifactId=example -Dpackage=org.example \
  -Dversion=1.0-SNAPSHOT -DinteractiveMode=false
```

## 4.1 Dependency configuration

Add the required dependencies for GraalPy in the dependency section of the POM.

`pom.xml`
```xml
<dependency>
    <groupId>org.graalvm.python</groupId>
    <artifactId>python</artifactId> <!-- ① -->
    <version>24.1.0</version>
    <type>pom</type> <!-- ② -->
</dependency>

<dependency>
    <groupId>org.graalvm.python</groupId>
    <artifactId>python-embedding</artifactId> <!-- ③ -->
    <version>24.1.0</version>
</dependency>
```

❶ The `python` dependency is a meta-package that transitively depends on all resources and libraries to run GraalPy.

❷ Note that the `python` package is not a JAR - it is simply a `pom` that declares more dependencies.

❸ The `python-embedding` dependency provides the APIs to manage and use GraalPy from Java.

## 4.2 Adding packages

Most Python packages are hosted on [PyPI](https://pypi.org) and can be installed via the `pip` tool.
The Python ecosystem has conventions about the filesystem layout of installed packages that need to be kept in mind when embedding into Java.
You can use the GraalPy plugin to manage Python packages for you.

`pom.xml`
```xml
    <build>
    <plugins>
        <plugin>
            <groupId>org.graalvm.python</groupId>
            <artifactId>graalpy-maven-plugin</artifactId>
            <version>24.1.0</version>
            <executions>
                <execution>
                    <configuration>
                        <packages> <!-- ① -->
                            <package>polyleven==0.8</package>
                        </packages>
                    </configuration>
                    <goals>
                        <goal>process-graalpy-resources</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

❶ The `packages` section lists all Python packages optionally with [requirement specifiers](https://pip.pypa.io/en/stable/reference/requirement-specifiers/).
In this case, we install the `polyleven` package and pin it to version `0.8`. Because we are not
specifying `<pythonResourcesDirectory>` the Maven plugin will embed the packages into the
resulting JAR as a standard Java resource.

## 4.3 Java Code

Open file `src/main/java/org/example/App.java` and replace the generated code
with the following:

`App.java`
```
package org.example;

import org.graalvm.polyglot.*;
import org.graalvm.python.embedding.utils.GraalPyResources;

import java.util.Set;

public class App {
    private static final Set<String> FRUITS = Set.of(
            "apple", "banana", "peach", "grape", "orange",
            "pear", "mango", "pineapple", "lemon", "lime", "apricot"); // ①

    public static void main(String[] args) {
        try (Context context = GraalPyResources.createContext()) { // ②
            Value pythonBindings = context.getBindings("python"); // ③
            pythonBindings.putMember("fruits", FRUITS.toArray()); // ④

            // ⑤
            context.eval("python", """
                    import polyleven
                    def get_similar_word(value):
                        words = [x for x in fruits if polyleven.levenshtein(x, value) <= 1] # ⑥
                        return words[0] if words else None
                    """);


            Value getSimilarWord = pythonBindings.getMember("get_similar_word"); // ⑦

            for (String value : args) {
                if (FRUITS.contains(value)) {
                    System.out.printf("✅ %s%n", value);
                } else {
                    System.out.printf("❌ %s", value);
                    Value similarWord = getSimilarWord.execute(value); // ⑧
                    if (!similarWord.isNull()) { // ⑨
                        System.out.printf(" (did you mean '%s')%n", similarWord.asString()); // ⑩
                    } else {
                        System.out.println();
                    }
                }
            }
        }
    }
}
```

❶ A Java set is used as our database of fruit names.

❷ GraalPy provides APIs to make setting up a context to load Python packages from Java as easy as possible.
Static method `GraalPyResources.createContext` will create a Context preconfigured to lookup Python
packages in Java resources, which should be generated by the GraalPy Maven plugin we added in
the previous section.

❸ Retrieve the Python "bindings". This is an object that provides access to Python global namespace.
One can read, add, or remove global variables using this object.

❹ Assign the Java set with fruit names to a global variable `fruits` in Python

❺ Execute Python code that defines a function named `get_similar_word`. Note that the Python code
imports our package `polyleven` and that the function `get_similar_word` references the global
variable `fruits`.

❻ The Java set exposed as global variable to Python can be iterated over using Python
list comprehension.

❼ Retrieve the Python function that was just defined. The object can be used to execute the function from Java.

❽ Execute the Python function from Java. The `execute` method takes variadic number of arguments.
Java String will appear as Python string to the Python function.

❾ Use the `Value.isNull` Java API to determine if the result is Python `None` value.

❿ Convert the result to a Java String representation.

## 5. Running the application

If you followed along with the example, you can now compile:

```shell
mvn package
```

The output should show messages from the installation of the `polyleven` package. The C code in
this package will be compiled as part of the installation. Python package maintainers
can choose to publish binary wheels (packaging format used in Python) for GraalPy, which would remove
the compilation step and a pre-built binary for your system would be just extracted from the wheel.

If there are any errors during the build, change the package in `pom.xml` from `polyleven` to some
pure Python package, such as `art==6.2` and run `mvn package` again. If a pure Python package
can be installed successfully, the problem is likely going to be your C compiler toolchain setup.
Look at the log of the failing installation for error messages that may help you to resolve the issue.

If the compilation is successful, you can now run your application from the commandline:

```shell
mvn exec:java -Dexec.mainClass=org.example.App -Dexec.args="appkle pear anana tomato"
[INFO] ...
❌ appkle (did you mean 'apple')
✅ pear
❌ anana (did you mean 'banana')
❌ strawberry
❌ tomato
```

## 6. Identify Native Extensions

It may not always be clear whether some specific Python package or some of its transitive dependencies
contain native extensions. Moreover, the native extensions may not be executed at runtime.
Since GraalPy support for native extensions is experimental and there are
some limitations when native extensions are used (see next section), it is useful to know
if and when the program loads native extensions. GraalPy has an option to warn when
an experimental feature is used. This log can be enabled with context options. Copy `App.java`
to create a new class `AppLogging.java` and update the context initialization code as follows:

`AppLogging.java`
```
try (Context context = GraalPyResources.contextBuilder()
        .option("log.python.capi.level", "WARNING") // ①
        .option("python.WarnExperimentalFeatures", "true") // ②
        .build()) {
```

❶ Set the log level to `WARNING` for loggers whose names begin with `capi.level`

❷ Set GraalPy option to log a warning every time a native extension is loaded.

When the application is run this time, it will print a waning that a native extension `polylevel`
is being loaded:

```shell
mvn exec:java -Dexec.mainClass=org.example.AppLogging -Dexec.args="appkle pear anana tomato"
[INFO] ...
[python::CExtContext] WARNING: Loading C extension module polyleven from
'/graalpy_vfs/venv/lib/python3.11/site-packages/polyleven.graalpy241dev156644dd29-311-native-x86_64-linux.so'.
Support for the Python C API is considered experimental.
...
```

In the log we can see that the `polyleven` C extension module was loaded and the filesystem path
that it was loaded from. Note that this is a Virtual Filesystem path. The shared library is extracted
to a temporary location on the real filesystem and loaded.

## 7. Native Extensions Limitations

## 7.1 Portability

As discussed in section 4.2. and as we saw in the log output in section 6, the Python package is bundled into
the resulting JAR as Java resource. Python native extensions are native shared libraries and as such they are
platform dependent, therefore the resulting JAR now becomes platform dependent and cannot be, for example,
packaged on Linux AMD64 and run on Apple M1.

It is possible to manually [build cross-platform JARs](https://github.com/oracle/graalpython/blob/master/docs/user/README.md#creating-cross-platform-jars-with-native-python-packages)
that contain resources for more than one system.

## 7.2 Single Context

Python extensions are often not built with multi-tenancy in mind and assume that they are initialized only
once per process and run only in a single Python interpreter instance. Because of that, for the time being,
GraalPy does not support loading native extensions in two or more contexts per JVM or native-image process.

To demonstrate this, copy `App.java` to `MultiContextApp.java` and change the code to inspect each argument
using a fresh new `Context`:

`MultiContextApp.java`
```
    public static String getSimilarWord(String value) {
        try (Context context = GraalPyResources.contextBuilder().build()) {
            context.eval("python", """
                    import polyleven
                    def get_similar_word(value):
                        words = [x for x in fruits if polyleven.levenshtein(x, value) <= 1]
                        return words[0] if words else None
                    """);

            Value pythonBindings = context.getBindings("python");
            pythonBindings.putMember("fruits", FRUITS.toArray());
            Value result = pythonBindings
                    .getMember("get_similar_word")
                    .execute(value);
            return result.isNull() ? null : result.asString();
        }
    }

    public static void main(String[] args) {
        for (String value : args) {
            if (FRUITS.contains(value)) {
                System.out.printf("✅ %s%n", value);
            } else {
                System.out.printf("❌ %s", value);
                String similarWord = getSimilarWord(value);
                if (similarWord != null) {
                    System.out.printf(" (did you mean '%s')%n", similarWord);
                } else {
                    System.out.println();
                }
            }
        }
    }
```

Run the application:

```shell
mvn package exec:java -Dexec.mainClass=org.example.MultiContextApp -Dexec.args="appkle pear anana strawberry tomato"
[INFO] ...
❌ appkle (did you mean 'apple')
✅ pear
❌ ananaTraceback (most recent call last):
  File "Unnamed", line 1, in <module>
SystemError: GraalPy option 'NativeModules' is set to false, but the 'llvm' language, which is required for this feature, is not available. If this is a GraalPy standalone distribution: this indicates internal error. If GraalPy was used as a Maven dependency: are you missing a runtime dependency 'org.graalvm.polyglot:llvm{-community}'?
```

The error refers to the LLVM runtime, which can be used as an execution engine for native extensions
and which provides multi-context support. However, using the LLVM runtime is not recommended, because
it requires compilation with specialized LLVM toolchain, has significant warm-up, memory footprint,
and its support may be removed in some future versions of GraalPy.


## 8. Next steps

- Use GraalPy with popular Java frameworks, such as [Spring Boot](../graalpy-spring-boot-guide/README.md) or [Micronaut](../graalpy-micronaut-guide/README.md)
- Follow along how you can manually [install Python packages and files](../graalpy-custom-venv-guide/README.md) if the Maven plugin gives not enough control
- [Freeze](../graalpy-freeze-dependencies-guide/README.md) transitive Python dependencies for reproducible builds
- [Migrate from Jython](../graalpy-jython-guide/README.md) to GraalPy


- Learn more about the GraalPy [Maven plugin](https://www.graalvm.org/latest/reference-manual/python/Embedding-Build-Tools/)
- Learn more about the Polyglot API for [embedding languages](https://www.graalvm.org/latest/reference-manual/embed-languages/)
- Explore in depth with GraalPy [reference manual](https://www.graalvm.org/latest/reference-manual/python/)