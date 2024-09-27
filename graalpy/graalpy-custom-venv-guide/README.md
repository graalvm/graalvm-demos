# Using Custom GraalPy Virtual Environment in a Java Application

While GraalPy provides a [python-embedding](https://central.sonatype.com/artifact/org.graalvm.python/python-embedding) package and GraalPy [Maven plugin](https://www.graalvm.org/latest/reference-manual/python/Embedding-Build-Tools/) simplify the required setup to ship Python packages
as Java resources or in separate folders, sometimes users may want to install the Python packages manually
using the GraalPy standalone distribution and then use the manually installed packages in Java.

This approach requires more effort, but may be useful in the following scenarios:

- User wants to have full control over the installation, including installation options, and the embedding `Context` setup.
- The required packages cannot be installed through the Maven integration due to compatibility issues. Some packages
make assumptions about the installation environment that are difficult or impossible to replicate in the Maven setup.

## 1. Getting Started

In this guide, we will add a small Python library to [generate ASCII art](https://pypi.org/project/art)
and use it from Java.

## 2. What you will need

To complete this guide, you will need the following:

* Some time on your hands
* A decent text editor or IDE
* A supported JDK[^1], preferably the latest [GraalVM JDK](https://graalvm.org/downloads/)

[^1]: Oracle JDK 17 and OpenJDK 17 are supported with interpreter only.
GraalVM JDK 21, Oracle JDK 21, OpenJDK 21 and newer with [JIT compilation](https://www.graalvm.org/latest/reference-manual/embed-languages/#runtime-optimization-support).
Note: GraalVM for JDK 17 is **not supported**.

## 3. Solution

We recommend that you follow the instructions in the next sections and create the application step by step.
However, you can go right to the [completed example](./).

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

Add the required dependencies for GraalPy in the `<dependencies>` section of the POM.

`pom.xml`
```xml
<dependencies>
  <dependency>
    <groupId>org.graalvm.polyglot</groupId>
    <artifactId>python</artifactId> <!-- ① -->
    <version>24.1.0</version>
    <type>pom</type> <!-- ② -->
  </dependency>

  <dependency>
    <groupId>org.graalvm.polyglot</groupId>
    <artifactId>polyglot</artifactId> <!-- ③ -->
      <version>24.1.0</version>
  </dependency>
</dependencies>
```

❶ The `python` dependency is a meta-package that transitively depends on all resources and libraries to run GraalPy.

❷ Note that the `python` package is not a JAR - it is simply a `pom` that declares more dependencies.

❸ The `polyglot` dependency provides the APIs to call Python from Java.

## 4.2 Custom Virtual Environment

In the Python ecosystem, it is common to use [virtual environments](https://docs.python.org/3/library/venv.html) to manage dependencies.
One can think of a virtual environment as a separate isolated Python installation, while under the hood, the environment shares
some parts with the Python installation that created it.

To create our virtual environment, [download GraalPy standalone](https://github.com/oracle/graalpython/releases/) distribution for your system.
Make sure to download exactly the same version as the version of the Maven dependencies we added above.
Extract the distribution on your system and invoke the `graalpy` command from it as follows:

```shell
> {path/to/graalpy-standalone}/bin/graalpy -m venv myvenv
```

This will create a new virtual environment in a directory called "myenv". Now we can install a Python
package into the virtual environment:

```shell
> . {path/to/graalpy-standalone}/bin/activate
> python --version
> pip install art==6.3
```

We can try using the new package from GraalPy REPL to see if it works as expected.
Continuing in the shell where we activated our virtual environment:

```
> python
Python 3.11.7 (Thu Sep 05 15:19:24 UTC 2024)
[Graal, Oracle GraalVM, Java 23 (amd64)] on linux
Type "help", "copyright", "credits" or "license" for more information.
>>> import art
>>> print(art.text2art("GraalPy"))
  ____                      _  ____         
 / ___| _ __   __ _   __ _ | ||  _ \  _   _ 
| |  _ | '__| / _` | / _` || || |_) || | | |
| |_| || |   | (_| || (_| || ||  __/ | |_| |
 \____||_|    \__,_| \__,_||_||_|     \__, |
                                      |___/ 
```

## 4.3 Creating a Python context

Now we switch back to Java. We will configure our embedding `Context` so that can load
packages from our virtual environment:

`App.java`
```java
package org.example;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.io.*;

import java.nio.file.*;

public class App {
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    public static void main(String[] args) {
        if (System.getProperty("venv") == null) {
            System.err.println("Provide 'venv' system property.");
            System.exit(1);
        }
        Path executable;
        if (IS_WINDOWS) { // ①
            executable = Paths.get(System.getProperty("venv"), "Scripts", "python.exe");
        } else {
            executable = Paths.get(System.getProperty("venv"), "bin", "python");
        }
        try (Context context = Context.newBuilder() // ②
                .option("python.Executable", executable.toAbsolutePath().toString()) // ③
                .option("python.ForceImportSite", "true") // ④
                .allowIO(IOAccess.newBuilder().allowHostFileAccess(true).build()) // ⑤
                .build()) {
            Value asciiArt = context.eval("python", "import art; art.text2art('GraalPy')"); // ⑥
            System.out.println(asciiArt.asString()); // ⑦
        }
    }
}
```

❶ We need a path to the python executable within the virtual environment. The path is different on Windows systems.

❷ Create a new `Context` builder

❸ Set option `python.Executable`, which will activate the virtual environment for our `Context`

❹ Set option `python.ForceImportSite`, which activates the `site.py` module,
which adds the packages installed in the virtual environment to the Python packages search path

❺ Allow host filesystem access, so that the Python code can actually load the packages from disk.
Alternatively, one can use `allowAllAccess(true)` to allow access to all resources. If you run
into errors, you can try running your code with `allowAllAccess(true)` to rule out a permission issue.

❻ Use the installed package to create a Python string with ASCII art text

❼ Print the Python string using Java `System.out`

## 5. Running the application

If you followed along with the example, you can now compile and run your application from the commandline:

```shell
./mvnw compile
./mvnw exec:java -Dexec.mainClass=org.example.App -Dvenv=/path/to/myvenv
```

Make sure to replace the `/path/to/myvenv` with the actual path where you created
the virtual filesystem on your system.

## 6. Next steps

- Use GraalPy with popular Java frameworks, such as [Spring Boot](../graalpy-spring-boot-guide/README.md) or [Micronaut](../graalpy-micronaut-guide/README.md)
- Install and use Python packages that rely on [native code](../graalpy-native-extensions-guide/README.md), e.g. for data science and machine learning
- [Migrate from Jython](../graalpy-jython-guide/README.md) to GraalPy
- [Freeze](../graalpy-freeze-dependencies-guide/README.md) transitive Python dependencies for reproducible builds


- Learn more about the GraalPy [Maven plugin](https://www.graalvm.org/latest/reference-manual/python/Embedding-Build-Tools/)
- Learn more about the Polyglot API for [embedding languages](https://www.graalvm.org/latest/reference-manual/embed-languages/)
- Explore in depth with GraalPy [reference manual](https://www.graalvm.org/latest/reference-manual/python/)
