# GraalVM Native Image Workshop

## Prerequisites

* OS : Linux or Mac OSX
* Apache Maven

## Introduction

We are going to use a fairly trivial application to walk through using GraalVM Native Image. Along the way we will see:

1. How you can turn a Java app into a native executable
2. How you can build a native image that works with the dynamic parts of Java
3. Using the command line tools for generating a native image, as well as using the Maven tooling

So what is going to be our testbed application? We will use a command line Java application that counts the number of files within the current directory and sub directories. As a nice extra it also calculates their total size.

As you work through this workshop you will need to update the code, in the following steps.

This workshop relies on using GraalVM 21.3 or higher, with GraalVM Native Image installed. It will work with either Community Edition (CE), or Enterprise Edition (EE). If there is any EE speific code / configuration, this will be clearly marked out.

Let's beign!

## Setup

Install GraalVM. Instructions can be found on the website:

[Linux](https://www.graalvm.org/docs/getting-started/linux/)
[macOS](https://www.graalvm.org/docs/getting-started/macos/)
[Windows](https://www.graalvm.org/docs/getting-started/windows/)

You will need the following installed:

* GraalVM 21.3.0 JDK 11 or JDK17 installed (either Enterprise Edition or Community Edition)
* Native Image plugin that matches your GraalVM installation

## Quick Note on Using Maven Profiles

This workshop will use Maven profiles. Proflies are a great way to have different build configurtions within a single `pom.xml` file. You can find out more about them [here](https://maven.apache.org/guides/introduction/introduction-to-profiles.html).

We will use several profiles throughout the project, each of which will serve a different purpose. We are able to call these profiles by passing a parameter containing the name of the profile to `mvn`. The example below shows how we would call the `JAVA` profile, when building with maven:

![User Input](./images/userinput.png)
```sh
$ mvn clean package -PJAVA
```

The name of the profile to be called is appended to the `-P` flag. We use these profiles within this workshop (all defined in the `pom.xml` file):

1. `java_agent` : This builds the Java application with the tracing agent. The agent tracks all usages of dynamic features of your application and captures this inforamtion into configuration files
2. `native` : This builds the native executable

## The Java App

The Java application can be found in the `src` directory and it consists of two files. 

* `App.java` : A wrapper for the `ListDir` application
* `ListDir.java` : This does all the work, counting the files and summarising the output

We build our application using a maven, `pom.xml` file. Now we have a basic understanding of our application, let's build our application and see how it works.

## Build the Basic Java App

First, check that the applicaiton builds the and works as expected.

From a command line:

![User Input](./images/userinput.png)
```sh
$ mvn clean package exec:exec
```

What the above command does:

1. Clean the project - get rid of any generated or compiled stuff.
2. Create a JAR file with the application in it. We will also be compiling an uber JAR.
3. Runs the application by running the `exec` plugin.

This should generate some output to the terminal. You should see the following included in the generated output (the number of files repoerted by the app may vary):

```sh
Counting directory: .
Total: 25 files, total size = 3.8 MiB
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

Did it work for you?

## Build a Native Image from the App

Next, we are going to build a native image version of the application. This will start quicker than the Java application.

We will do this by hand first, using the `native-image` tool. To begin, check that you have a compiled uber JAR in your `target` dir:

![User Input](./images/userinput.png)
```sh
$ ls ./target
  drwxrwxr-x 1 krf krf    4096 Mar  4 11:12 archive-tmp
  drwxrwxr-x 1 krf krf    4096 Mar  4 11:12 classes
  drwxrwxr-x 1 krf krf    4096 Mar  4 11:12 generated-sources
  -rw-rw-r-- 1 krf krf  496273 Mar  4 11:38 graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar
  -rw-rw-r-- 1 krf krf    7894 Mar  4 11:38 graalvmnidemos-1.0-SNAPSHOT.jar
  drwxrwxr-x 1 krf krf    4096 Mar  4 11:12 maven-archiver
  drwxrwxr-x 1 krf krf    4096 Mar  4 11:12 maven-status
```

The file you will need is, `graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar`.

**NOTE** : You need to have Native Image plugin installed in GraalVM - see the set up instructions.

Now you can generate a native image as follows, within the root of the project:

![User Input](./images/userinput.png)
```sh
$ native-image -jar ./target/graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar --no-fallback -H:Class=oracle.App -H:Name=file-count
```

This will generate a file called `file-count`, which you can run as follows:

![User Input](./images/userinput.png)
```
./file-count
```

Try timing it:

![User Input](./images/userinput.png)
```sh
time ./file-count
```

Compare that to the time running the app with the regular `java` command:

![User Input](./images/userinput.png)
```sh
time java -cp ./target/graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar oracle.App
```

What do the various parameters we passed to the `native-image` command do? The full documentation on these can be found [here](https://www.graalvm.org/reference-manual/native-image/BuildConfiguration/):

* `--no-fallback`: Do not generate a fallback image. A fallback image requires the JVM to run, and we do not want this. We just want it to be a native image.
* `-H:Class`: Tell the `native-image` builder which class is the entry point method (the `main` method).
* `-H:Name`: Specify what the output executable file should be called.

We can also run the `native-image` tool using Maven. If you look at the `pom.xml` file in the project, you should find the following snippet:

```xml
<!-- Native Image -->
<plugin>
    <groupId>org.graalvm.buildtools</groupId>
    <artifactId>native-maven-plugin</artifactId>
    <version>${native.maven.plugin.version}</version>
    <extensions>true</extensions>
    <executions>
    <execution>
        <id>build-native</id>
        <goals>
        <goal>build</goal>
        </goals>
        <phase>package</phase>
    </execution>
    </executions>
    <configuration>
        <!-- Set this to true if you need to switch this off -->
        <skip>false</skip>
        <!-- The output name for the executable -->
        <imageName>${exe.file.name}</imageName>
        <mainClass>${app.main.class}</mainClass>
        <buildArgs>
            <!-- Don't create a native executable that falls back to starting a JVM if the image won't build -->
            <buildArg>--no-fallback</buildArg>
            <!-- Warn about features that arent supported by native image at run time.
                If there is any reflection, for example, this will generate an error at the runtime of
                native image.
            -->
            <buildArg>--report-unsupported-elements-at-runtime</buildArg>
        </buildArgs>
    </configuration>
</plugin>
```

The Native Image Maven plugin does the heavy lifting of running the native image build. It can always be turned off using the `<skip>true</skip>` tag. Note also that we can pass parameters to `native-image` through the `<buildArgs />` tag.

To build the native image using maven:

![User Input](./images/userinput.png)
```sh
$ mvn clean package -Pnative
```

**Note**: The maven build places the executable, `file-count`, in the `target` directory.

## Add Log4J and Why We Need the Tracing Agent

So far, so good. But say we now we want to add a library, or some code, to our project that
relies on reflection. A good candidate for testing this out would be to add `log4j`. Let's do that.

We've already added it as a dependency in the `pom.xml` file, and it can be found in the dependencies. Please uncomment it:

```xml
<!-- <dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency> -->
```

To add `log4j` all we need to do is to open up the `ListDir.java` file and uncomment some things in order to start using it. Go through and uncomment the various lines that add the imports and the logging code. Uncomment the following lines:

```java
//import org.apache.log4j.Logger;
```

```java
        /*
        // Add some logging
        if(logger.isDebugEnabled()){
			logger.debug("Processing : " + dirName);
        }
        */
```

```java
                /*
                // Add some logging
                if(logger.isDebugEnabled()){
                    logger.debug("Processing : " + f.getAbsolutePath());
                }
                */
```

OK, so now we have added logging, let's see if it works by rebuilding and running our Java app:

![User Input](./images/userinput.png)
```sh
$ mvn clean package exec:exec
```

Great, that works. Now, let's build a native image using the Maven profile:

![User Input](./images/userinput.png)
```sh
$ mvn clean package -Pnative
```

Then run the executable:

![User Input](./images/userinput.png)
```sh
$ ./target/file-count
```

This generates an error:

```java
Exception in thread "main" java.lang.NoClassDefFoundError
        at org.apache.log4j.Category.class$(Category.java:118)
        at org.apache.log4j.Category.<clinit>(Category.java:118)
        at java.lang.Class.ensureInitialized(DynamicHub.java:552)
        at oracle.ListDir.<clinit>(ListDir.java:75)
        at oracle.App.main(App.java:63)
Caused by: java.lang.ClassNotFoundException: org.apache.log4j.Category
        at java.lang.Class.forName(DynamicHub.java:1433)
        at java.lang.Class.forName(DynamicHub.java:1408)
        ... 5 more
```

Why? This is caused by the addition of the Log4J library. This library depends heavily upon the reflection calls,
and when the native image is generated, the builder performs an aggressive static analysis to see what is being called. Anything that is not called, the builder assumes as not needed. This is a "closed world" assumption. The native image builder concludes that no reflection is taking place. We need to let the native image tool know about the reflection calls.

We could do this by hand, but luckily we do not have to. The GraalVM Java runtime comes with
a tracing agent that will do this for us. It generates a number of JSON files that
map all the cases of reflection, JNI, proxies and resources accesses that it can locate.

For this case, it will be sufficient to run this only once, as there is only one path through the
application, but we should bear in mind that we may need to do this a number of times with
different program input. A complete documentation on this can be found [here](https://www.graalvm.org/reference-manual/native-image/Agent/).

The way to generate these JSON files is to add the following to the command line that is running your Java application.
Note that the agent parameters **MUST** come before any `-jar` or `-classpath` paremetrs. Also note that we specify a directory into which we would like to put the output.
The recommended location is under `META-INF/native-image`.

```sh
src/main/resources/META-INF/native-image
```

If we place these files in this location the native image builder will pick them up automaticatly.
Run the tracing agent:

![User Input](./images/userinput.png)
```sh
$ java -agentlib:native-image-agent=config-output-dir=./src/main/resources/META-INF/native-image -cp ./target/graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar oracle.App
```

The files should now be present.

**Note**: There is a Maven profile added for this reason that can be called as follows:

![User Input](./images/userinput.png)
```sh
$ mvn clean package exec:exec -Pjava_agent
```

Now, you run the native image generation again, and then execute the generated image:

![User Input](./images/userinput.png)
```sh
$ mvn package -Pnative
$ time ./target/.file-count
```

We should see that it works and that it also produces log messages.

## Note on Configuring Native Image Generation

We can also pass parameters to the native image tool using a properties files that typically lives in:

```sh
src/main/resources/META-INF/native-image/native-image.properties
```

In this demo we have included one such file in order to give you an idea of what you can do with it.

## Conclusion

We have demonstrated a few of the capabilities of GRAALVM's native image funcitonality, including:

1. How to generate a fast native image from a Java command line app
2. How to use maven to build a native image
3. How to use the tracing agent to automate the process of finding relfection calls in our code

We hope that this has been useful. Good luck and start using Native Image!
