# Microanut Layered Native Image Demo

This example shows how to build a simple [Micronaut](https://micronaut.io/) REST application using the [GraalVM Native Image Layers](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/imagelayer/NativeImageLayers.md) feature.

## Environment Setup
Point your `JAVA_HOME` to a GraalVM distribution.
Native Image Layers is an experimental feature, for best experience use the latest [GraalVM Early Access Build](https://github.com/graalvm/oracle-graalvm-ea-builds/releases).
```bash
export JAVA_HOME=/path/to/graalvm/ea/build
```

## Create The Micronaut Application

We'll start by generating a basic application using the Micronaut CLI.
For more details see the [Micronaut guide](https://guides.micronaut.io/latest/creating-your-first-micronaut-app-maven-java.html).

First we need to install the `mn` tool:
```bash
sdk install micronaut 4.9.4
sdk use micronaut 4.9.4
```

Now we ca generate the basic app:
```bash
mn create-app example.micronaut.micronaut-hello-rest-maven-layered --build=maven --lang=java --features=graalvm
```

### Add A Custom Controller

We'll add a custom controller to `src/main/java/example/micronaut/HelloController.java`:
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

We'll first demonstrate how to build a standalone executable for this simple app.
For this we'll extend the `pom.xml` with a custom profile and configure the native build using the [GraalVM Native Image Maven plugin](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html):
```xml
<profile>
    <id>standalone</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
                <version>0.10.3</version>
                <configuration>
                    <imageName>standalone-app</imageName>
                    <mainClass>example.micronaut.Application</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

Using this profile we can now generate the executable:
```bash
./mvnw clean package -Dpackaging=native-image -Pstandalone
```

This will generate an executable file that we can run
```bash
./target/standalone-app
 __  __ _                                  _   
|  \/  (_) ___ _ __ ___  _ __   __ _ _   _| |_ 
| |\/| | |/ __| '__/ _ \| '_ \ / _` | | | | __|
| |  | | | (__| | | (_) | | | | (_| | |_| | |_ 
|_|  |_|_|\___|_|  \___/|_| |_|\__,_|\__,_|\__|
12:20:53.437 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 6ms. Server Running: http://localhost:8080
```
and test our custom endpoint
```bash
curl localhost:8080/hello
Hello from GraalVM Native Image!
```

### Layered Application

#### Configure The Base Layer

We will create a base layer that contains both `java.base` and the Micronaut framework.
For this we'll add a second custom profile:
```xml
<profile>
    <id>base-layer</id>
    <build>
    <directory>${project.basedir}/base-layer-target</directory>
        <plugins>
            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
                <version>0.10.3</version>
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

We use `-H:LayerCreate=` to specify what should be included in the base layer: `java.base`, `io.micronaut`, `io.netty` and a few more other packages that a Micronaut application usually depends on.
For more details consult the [Native Image Layers documentation](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/imagelayer/NativeImageLayers.md).

Additionally we use two options that are specific for layered builds: `-H:ApplicationLayerOnlySingletons=`, which specifies that a singleton object should be installed in the application layer only, and `-H:ApplicationLayerInitializedClasses=`, which registers a class as being initialized in the app layer.
These are necessary for correctly building the Micronaut framework in a layered set-up. 

Now we can build the base layer:
```bash
./mvnw clean install -Dpackaging=native-image -Pbase-layer
```
This will create the `base-layer.nil` which is a build time dependency for the application build.
It will also create the `libmicronautbaselayer.so` shared library which is a run time dependency for the application layer.
Note also that we use `install` instead of `package` to ensure that the base layer jar is installed in the `.m2` cache as it will be needed by the application build later.


### Configure The Application Layer

To configure the app layer we'll add an additional profile:
```xml
<profile>
    <id>app-layer</id>
    <build>
        <directory>${project.basedir}/app-layer-target</directory>
        <plugins>
            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
                <version>0.10.3</version>
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

Now we can build a layered Native Image which depends on the base layer that we created earlier:
```bash
./mvnw clean package -Dpackaging=native-image -Papp-layer
```

This will generate the layered executable file in `./app-layer-target/layered-app` and will copy `libmicronautbaselayer.so` next to it.

Then we can execute the layered application:
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
Hello from GraalVM Native Image!
```
