# GraalVM Native Image Workshop

## Introduction

This workshop aims to walk you through a number of the features of GraalVM's Native Image,
namely:

1. Run a basic Java app
2. Make a native Image from it and see how that compares in terms of startup time
3. Look at how we use the tracing agent to identify reflection etc.
4. How we can run some of the application at build time

## Setup

Please install GraalVM. Instructions can be found [here](./SETUP-QUICK.md).

You need a working version of Maven as well, and this should be covered in the installation
instructions.

## Outline of Demo

We are going to use a fairly trivial application to walk through some of the features that
are available within GraalVM Native Image. These are:

1. How you can turn a Java app into a native executable
2. How you deal with reflection etc.
3. Using the command line tools for generating a native image, as well as using the maven tooling

The application code that you have checked out builds a Java command line app that
counts the number of files within the current directory and sub directories. It also calculates their total size.

You will need to update the code, in the following various steps, in order to work through the
points we listed above.

Let's beign!

## Quick Note on Using Maven Profile

The maven build has been split into several different profiles, each of which serves a different purpose. We can call these profiles by passing a parameter that contains the name of the profile to maven. In the example below the `JAVA` profile is called:

![User Input](./images/userinput.png)
```sh
$ mvn clean package -PJAVA
```

The name of the proflie to be called is appended to the `-P` flag. We have the following profiles defined in the maven file:

1. `JAVA` : This builds the Java applicaiton
2. `JAVA_AGENT_LIB` : Ths builds the Java application with agent tracing. More on this later
3. `NATIVE_IMAGE` : This builds the native image

## Build the Basic Java App

So first off, let's check that we can build the application and that it works as expected.

From a command line:

![User Input](./images/userinput.png)
```sh
$ mvn clean package exec:exec -PJAVA
```

What the above does:

1. Clean the project - gets rid of any generated, or compiled, stuff
2. Create a Jar with our application in it. We will also be compiling an uber jar
3. Runs the application by running the exec plugin

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

OK. Now let's build a native image version of our application.

We will do this by hand first. First check that you have a compiled uber jar in your `target` dir:

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

The file we want is, `graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar`.

Now we can generate a native image as follows, within the root of the project:

![User Input](./images/userinput.png)
```sh
$ native-image -jar ./target/graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar --no-fallback --no-server -H:Class=oracle.App -H:Name=file-count
```

This will generate a file called, `file-count`, which you can run as follows:

![User Input](./images/userinput.png)
```
./file-count
```

Try timing it:

![User Input](./images/userinput.png)
```sh
time ./file-count
```

Compare that to the time running the app as Java:

![User Input](./images/userinput.png)
```sh
time java -cp ./target/graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar oracle.App
```

What do the various parameters we passed to the `native-image` command do? Full documentation on these can be found [here](https://www.graalvm.org/docs/reference-manual/native-image/#image-generation-options):

* `--no-server` : Don't start a build server process. For our examples we just want to run the builds. It is possible to run the builds through a build server,
* `--no-fallback` : Don't generate a fallback image. A fallback image requires the JVM and we generally don't want this, we just want it to be a native image
* `-H:Class` : Tells the native-image tool which class is with the entry point method (main)
* `-H:Name` : This specifies what the output executable should be called

We can also run the `native-image` tool using maven. If you look at the `pom.xml` file in the project you should be able to find the following snippet:

```xml
<!-- Native Image -->
<plugin>
    <groupId>org.graalvm.nativeimage</groupId>
    <artifactId>native-image-maven-plugin</artifactId>
    <version>${graalvm.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>native-image</goal>
            </goals>
            <phase>package</phase>
        </execution>
    </executions>
    <configuration>
        <!-- Set this to true if you need to switch this off -->
        <skip>false</skip>
        <!-- The output name for the executable -->
        <imageName>${exe.file.name}</imageName>
        <!-- Set any parameters we need to pass to the native-image tool.
            no-fallback : create a native image that doesn't fall back to the JVM
            no-server : don't start a build server, which you then just need to shut down,
                        in order to build the image
        -->
        <buildArgs>
            --no-fallback --no-server
        </buildArgs>
    </configuration>
</plugin>
```

This plugin does the heavy lifting of running the native image build. It can always be turned off using the `<skip>true</skip>` tag. Note also that we can pass parameters to `native-image` through the `<buildArgs />` tag.

To build the native image using maven:

![User Input](./images/userinput.png)
```sh
$ mvn clean package -PNATIVE_IMAGE
```

**NOTE** : The maven build places the executable, `file-count`, in the `target` directory.

## Add Log4J and Why We Need the Tracing Agent

So far, so good. But say we now we want to add a library, or some code, to our project that
relies on reflection. A good candidate for testing this out would be to add `log4j`. Let's do that.

We've already added it as a dependency in the `pom.xml` file, and it can be seen in the depencies:

```xml
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
```

To add `log4j` all we need to do is to open up the `ListDir.java` file and uncomment some things in order to start using it. Go through and uncomment the various lines that add the imports and the logging code. These are the following lines that need uncommenting:

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
$ mvn clean package exec:exec -PJAVA
```

Great, that works. Now, let's build a native image using the maven profile:

![User Input](./images/userinput.png)
```sh
$ mvn clean package -PNATIVE_IMAGE
```

The run the built image:

![User Input](./images/userinput.png)
```sh
$ ./target/file-count
```

This generates an error:

```java
Exception in thread "main" java.lang.NoClassDefFoundError
        at org.apache.log4j.Category.class$(Category.java:118)
        at org.apache.log4j.Category.<clinit>(Category.java:118)
        at com.oracle.svm.core.hub.ClassInitializationInfo.invokeClassInitializer(ClassInitializationInfo.java:350)
        at com.oracle.svm.core.hub.ClassInitializationInfo.initialize(ClassInitializationInfo.java:270)
        at java.lang.Class.ensureInitialized(DynamicHub.java:496)
        at com.oracle.svm.core.hub.ClassInitializationInfo.initialize(ClassInitializationInfo.java:235)
        at java.lang.Class.ensureInitialized(DynamicHub.java:496)
        at oracle.ListDir.<clinit>(ListDir.java:75)
        at com.oracle.svm.core.hub.ClassInitializationInfo.invokeClassInitializer(ClassInitializationInfo.java:350)
        at com.oracle.svm.core.hub.ClassInitializationInfo.initialize(ClassInitializationInfo.java:270)
        at java.lang.Class.ensureInitialized(DynamicHub.java:496)
        at oracle.App.main(App.java:63)
Caused by: java.lang.ClassNotFoundException: org.apache.log4j.Category
        at com.oracle.svm.core.hub.ClassForNameSupport.forName(ClassForNameSupport.java:60)
        at java.lang.Class.forName(DynamicHub.java:1211)
        ... 12 more
```

Why? This is caused by our addition of the log4j library. It depends heavily upon reflection
and when we generate the native image we do a lot of aggressive analysis to see what is being called. Anything that isn't called, we assume is not needed. This is a "closed World" assumption. We assume that no reflection is taking place. So we need to let the native image tool know about this.

We could do this by hand, but luckily we don't have to. The GraalVM Java runtime comes with
a tracing agent that will do this for us. It generates a number of JSON files that
map all the cases of reflection, JNI, proxies and resources that it can locate.

For our case it will be sufficient to run this only once, as there is only one path through our
application, but we should bear in mind that we may need to do this a number of times with
different program input. Full docs on this can be found [here](https://www.graalvm.org/docs/reference-manual/native-image/#tracing-agent).

The way to generate these JSON files is to add the following to the command line that is running
your Java application. Notes that the agent params **MUST** come before any jar or classpath paremetrs. Also note that we specify a directory into which we would like to put the output.
I have placed it into the source tree at:

```sh
src/main/resources/META-INF/native-image
```

If we place these files in this location the native image tooling will pick them up automaticatly.

So to run the tracing agent:

![User Input](./images/userinput.png)
```sh
$ java -agentlib:native-image-agent=config-output-dir=./src/main/resources/META-INF/native-image -cp ./target/graalvmnidemos-1.0-SNAPSHOT-jar-with-dependencies.jar oracle.App
```

The files should now be present.

***Note**: I have also added a maven profile that will do this for you and this can be called as follows:

![User Input](./images/userinput.png)
```sh
$ mvn clean package exec:exec -PJAVA_AGENT_LIB
```

Now if we run the native image generation again and then run the generated image:

![User Input](./images/userinput.png)
```sh
$ mvn package -PNATIVE_IMAGE
$ time ./target/.file-count
```

We should see that it works and that it also produces log messages.

## Note on Configuring Native Image Generation

We can also pass parameters to the native image tool using a properties files that typically lives in:

```sh
src/main/resources/META-INF/native-image/native-image.properties
```

In this demo we have included one such file in order to give you an idea of what you can do with this file.

## Conclusion

We've seen a few of the capabilities of GRAALVM's native image funcitonality, including:

1. Generating a fast native image from a Java command line app
2. How to use maven to build a native image
3. How to use the tracing agent to automate the process of finding relfection in our code

We hope that this has been useful. Good Luck and start using Native Image!
