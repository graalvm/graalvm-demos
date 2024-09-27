# Pinning Python Dependencies

Python libraries can be used in and shipped with plain Java applications. 

The [GraalPy Maven artifacts](https://central.sonatype.com/artifact/org.graalvm.polyglot/python) and [GraalVM Polyglot APIs](https://www.graalvm.org/latest/reference-manual/embed-languages/) allow flexible integration with different project setups.

Using Python packages in Java projects often requires a bit more setup, due to the nature of the Python packaging ecosystem.
GraalPy provides a [python-embedding](https://central.sonatype.com/artifact/org.graalvm.python/python-embedding) package that simplifies the required setup to ship Python packages as Java resources or in separate folders.
The important entry points to do so are the [VirtualFileSystem](https://github.com/oracle/graalpython/blob/master/docs/user/Embedding-Build-Tools.md#virtual-filesystem) and the [GraalPyResources](https://github.com/oracle/graalpython/blob/master/docs/user/Embedding-Build-Tools.md#deployment) classes.

Unlike with Java libraries, Python packages frequently specify their dependencies as a range
rather than one specific version. This can create issues during development and testing, because
transitive set of dependencies may change when some package publishes a new release.

We recommend pinning all transitive dependencies to single version and upgrade them
manually in a controlled fashion. This guide shows how this can be done with the
GraalPy Maven plugin. We will install package `vaderSentiment`, discover all its
transitive dependencies and then pin them in the Maven.

## 1. What you will need

To complete this guide, you will need the following:

* Some time on your hands
* A decent text editor or IDE
* A supported JDK[^1], preferably the latest [GraalVM JDK](https://graalvm.org/downloads/)

[^1]: Oracle JDK 17 and OpenJDK 17 are supported with interpreter only.
GraalVM JDK 21, Oracle JDK 21, OpenJDK 21 and newer with [JIT compilation](https://www.graalvm.org/latest/reference-manual/embed-languages/#runtime-optimization-support).
Note: GraalVM for JDK 17 is **not supported**.

## 2. Solution

We recommend that you follow the instructions in the next sections and create the application step by step.
However, you can go right to the [completed example](https://github.com/graalvm/graalpy-demos/tree/master/nativext).

## 3. Writing the application

You can start with any Maven application that runs on JDK 17 or newer.
We will use a default Maven application [generated](https://maven.apache.org/archetypes/maven-archetype-quickstart/) from an archetype.

```shell
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes \
  -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 \
  -DgroupId=example -DartifactId=example -Dpackage=org.example \
  -Dversion=1.0-SNAPSHOT -DinteractiveMode=false
```

## 4. Adding packages

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
                            <package>vaderSentiment==3.3.2</package>
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
In this case, we install the `vaderSentiment` package and pin it to version `3.3.2`. Because we are not
specifying `<pythonResourcesDirectory>` the Maven plugin will embed the packages into the
resulting JAR as a standard Java resource.

## 5. Determining and Pinning Transitive Dependencies

Package the application using Maven, this should install all the transitive dependencies
in a newly created [virtual environment](https://docs.python.org/3/library/venv.html):

```shell
mvn package
```

If the compilation is successful, one can run the following command to get
versions of all the installed Python packages:

```shell
./target/classes/org.graalvm.python.vfs/venv/bin/pip3 freeze -l
certifi==2024.8.30
charset-normalizer==3.1.0
idna==3.8
requests==2.32.3
urllib3==2.2.2
vaderSentiment==3.3.2
```

Copy and paste the package names and versions to the `pom.xml`, and wrap
them in `<package>` xml tag:

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
                            <package>vaderSentiment==3.3.2</package>
                            <package>certifi==2024.8.30</package>
                            <package>charset-normalizer==3.1.0</package>
                            <package>idna==3.8</package>
                            <package>requests==2.32.3</package>
                            <package>urllib3==2.2.2</package>
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

Note: one can use other Python tools, such as `pipdeptree` to generate the following
dependency tree, where we can also see the version ranges.

```
vaderSentiment==3.3.2
└── requests [required: Any, installed: 2.32.3]
    ├── certifi [required: >=2017.4.17, installed: 2024.8.30]
    ├── charset-normalizer [required: >=2,<4, installed: 3.1.0]
    ├── idna [required: >=2.5,<4, installed: 3.8]
    └── urllib3 [required: >=1.21.1,<3, installed: 2.2.2]
```

*Warning:
Is it not recommended to manually alter the virtual environment.
Any changes will be overridden by the GraalPy Maven plugin.*

## 8. Next steps

- Use GraalPy in a [Java SE application](../graalpy-javase-guide/README.md)
- Use GraalPy and `vaderSentiment` with popular Java frameworks, such as [Spring Boot](../graalpy-spring-boot-guide/README.md) or [Micronaut](../graalpy-micronaut-guide/README.md)
- Install and use Python packages that rely on [native code](../graalpy-native-extensions-guide/README.md), e.g. for data science and machine learning
- Follow along how you can manually [install Python packages and files](../graalpy-custom-venv-guide/README.md) if the Maven plugin gives not enough control
- [Freeze](../graalpy-freeze-dependencies-guide/README.md) transitive Python dependencies for reproducible builds
- [Migrate from Jython](../graalpy-jython-guide/README.md) to GraalPy


- Learn more about the GraalPy [Maven plugin](https://www.graalvm.org/latest/reference-manual/python/Embedding-Build-Tools/)
- Learn more about the Polyglot API for [embedding languages](https://www.graalvm.org/latest/reference-manual/embed-languages/)
- Explore in depth with GraalPy [reference manual](https://www.graalvm.org/latest/reference-manual/python/)