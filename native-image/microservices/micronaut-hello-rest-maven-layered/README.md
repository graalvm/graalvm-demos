# Micronaut Layered Native Image Demo

This example shows how to build a simple [Micronaut](https://micronaut.io/) REST application using the [GraalVM Native Image Layers](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/imagelayer/NativeImageLayers.md) feature.

### Prerequisites
- SDKMAN! (for installing Micronaut CLI)
- Linux x64
- Latest GraalVM 25.1 EA build (with Native Image support)

> Native Image Layers is an experimental feature. For the best experience use the latest [GraalVM Early Access Build](https://github.com/graalvm/oracle-graalvm-ea-builds/releases).

## Environment Setup
Point your `JAVA_HOME` to the GraalVM distribution.
```bash
export JAVA_HOME=/path/to/graalvm/ea/build
```

Install `mn`, the Micronaut CLI tool:
```bash
sdk install micronaut 4.9.4
sdk use micronaut 4.9.4
```

## Create the Micronaut Application

Start by generating a basic application using the Micronaut CLI.
For more details, see the [Micronaut guide](https://guides.micronaut.io/latest/creating-your-first-micronaut-app-maven-java.html).

```bash
mn create-app example.micronaut.micronaut-hello-rest-maven-layered --build=maven --lang=java --features=graalvm
```

This creates a new Micronaut project with the following structure:
```
micronaut-hello-rest-maven-layered/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── example/micronaut/Application.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── mvnw (Maven wrapper)
```

For executing the subsequent commands, enter the project directory:
```bash
cd micronaut-hello-rest-maven-layered
```

### Add a Custom Controller

Add a custom controller to `src/main/java/example/micronaut/HelloController.java`:
```java
package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/hello")
public class HelloController {
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from GraalVM Native Image!";
    }
}
```

### Standalone Application

First, build a standalone executable for this simple application.
For this, extend the `pom.xml` with a custom profile and configure the native build using the [GraalVM Native Image Maven plugin](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html):
```xml
<profile>
    <id>standalone</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
                <version>${native.plugin.version}</version>
                <configuration>
                    <imageName>standalone-app</imageName>
                    <mainClass>example.micronaut.Application</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

Using this profile you can now generate the executable:
```bash
./mvnw clean package -Dpackaging=native-image -Pstandalone
```

This will generate an executable file that you can run:
```bash
./target/standalone-app
 __  __ _                                  _
|  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_
| |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
| |  | | | (__| | | (_) | | | | (_| | |_| | |_
|_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|
12:20:53.437 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 6ms. Server Running: http://localhost:8080
```
and test your custom endpoint:
```bash
curl localhost:8080/hello
```
The expected output is:
```
Hello from GraalVM Native Image!
```

### Layered Application

#### Configure the Base Layer

Next, create a base layer that contains both `java.base` and the Micronaut framework.
For this, add a second custom profile:
```xml
<profile>
    <id>base-layer</id>
    <build>
    <directory>${project.basedir}/base-layer-target</directory>
        <plugins>
            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
                <version>${native.plugin.version}</version>
                <configuration>
                    <imageName>libmicronautbaselayer</imageName>
                    <mainClass>.</mainClass>
                    <buildArgs>
                        <buildArg>-H:+UnlockExperimentalVMOptions</buildArg>
                        <buildArg>-H:LayerCreate=base-layer.nil,module=java.base,package=io.micronaut.*,package=io.netty.*,package=jakarta.*,package=com.fasterxml.jackson.*,package=org.slf4j.*,package=reactor.*,package=org.reactivestreams.*</buildArg>
                        <buildArg>-H:ApplicationLayerOnlySingletons=io.micronaut.core.io.service.ServiceScanner$StaticServiceDefinitions</buildArg>
                        <buildArg>-H:ApplicationLayerInitializedClasses=io.micronaut.inject.annotation.AnnotationMetadataSupport</buildArg>
                        <buildArg>-H:ApplicationLayerInitializedClasses=io.micronaut.core.io.service.MicronautMetaServiceLoaderUtils</buildArg>
                        <buildArg>-H:-UnlockExperimentalVMOptions</buildArg>
                    </buildArgs>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

The `-H:LayerCreate=` option is used to specify what should be included in the base layer: `java.base`, `io.micronaut`, `io.netty` and a few more other packages that a Micronaut application usually depends on.
For more details, consult the [Native Image Layers documentation](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/imagelayer/NativeImageLayers.md).

Additionally, two more options are used, specific for layered builds:
* `-H:ApplicationLayerOnlySingletons=`, which specifies that a singleton object should be installed in the application layer only,
* `-H:ApplicationLayerInitializedClasses=`, which registers a class as being initialized in the app layer.

These are necessary for correctly building the Micronaut framework in a layered set-up.

Now you can build the base layer:
```bash
./mvnw clean install -Dpackaging=native-image -Pbase-layer
```
This will create the `base-layer.nil` which is a build time dependency for the application build.
It will also create the `libmicronautbaselayer.so` shared library which is a run time dependency for the application layer.
Note also that you use `install` instead of `package` to ensure that the base layer JAR is installed in the `.m2` cache as it will be needed by the application build later.

### Configure The Application Layer

To configure the application layer, add an additional profile:
```xml
<profile>
    <id>app-layer</id>
    <build>
        <directory>${project.basedir}/app-layer-target</directory>
        <plugins>
            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
                <version>${native.plugin.version}</version>
                <configuration>
                    <imageName>layered-app</imageName>
                    <mainClass>example.micronaut.Application</mainClass>
                    <buildArgs>
                        <buildArg>-H:+UnlockExperimentalVMOptions</buildArg>
                        <buildArg>-H:LayerUse=base-layer-target/base-layer.nil</buildArg>
                        <buildArg>-H:LinkerRPath=$ORIGIN</buildArg>
                        <buildArg>-H:-UnlockExperimentalVMOptions</buildArg>
                    </buildArgs>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

Now you can build a layered native image which depends on the base layer that you created earlier:
```bash
./mvnw clean package -Dpackaging=native-image -Papp-layer
```

This will generate the layered executable file in `./app-layer-target/layered-app` and will copy `libmicronautbaselayer.so` next to it.

Then you can execute the layered application:
```bash
./app-layer-target/layered-app
 __  __ _                                  _
|  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_
| |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
| |  | | | (__| | | (_) | | | | (_| | |_| | |_
|_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|
12:24:21.341 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 6ms. Server Running: http://localhost:8080
```
and test it with:
```
curl localhost:8080/hello
```
The expected output is:
```
Hello from GraalVM Native Image!
```

### Learn More

* [Native Image Layers](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/imagelayer/NativeImageLayers.md)]