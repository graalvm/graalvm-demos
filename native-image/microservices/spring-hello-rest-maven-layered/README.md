# Spring Layered Native Image Demo

This example shows how to build a simple [Spring](https://spring.io/) REST application using the [GraalVM Native Image Layers](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/imagelayer/NativeImageLayers.md) feature.

### Prerequisites
- Linux x64
- Latest GraalVM 25.1 EA build (with Native Image support)

> Native Image Layers is an experimental feature. For the best experience use the latest [GraalVM Early Access Build](https://github.com/graalvm/oracle-graalvm-ea-builds/releases).

## Environment Setup
Point your `JAVA_HOME` to the GraalVM distribution.
```bash
export JAVA_HOME=/path/to/graalvm/ea/build
```

## Create the Spring Application

Start by generating a basic application using the [online generator](https://start.spring.io/).
For more details, see the [Spring guide](https://spring.io/guides/gs/spring-boot).

On the web page, choose `Maven` for the `Project`. Then choose `Java` for `Language`.

Then choose any version, and any name for the project.

Finally, use `Jar` for `Packaging`.

This creates a new Spring project with the following structure:
```
demo/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── example/demo/DemoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── mvnw (Maven wrapper)
```

For executing the subsequent commands, enter the project directory:
```bash
cd spring-hello-rest-maven-layered
```

### Add a Custom Controller

Add a custom controller to `src/main/java/com/example/demo/HelloController.java`:
```java
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
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
                <version>${native.maven.plugin.version}</version>
                <configuration>
                    <imageName>spring-demo</imageName>
                    <mainClass>com.example.demo.DemoApplication</mainClass>
					<buildArgs combine.children="append">
						<buildArg>--verbose</buildArg>
					</buildArgs>
				</configuration>
			</plugin>
        </plugins>
    </build>
</profile>
```

Using this profile you can now generate the executable:
```bash
cd spring-demo
../mvnw clean package -Pnative -Pstandalone
```

This will generate an executable file that you can run:
```bash
./target/spring-demo

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.0.2)
```
Test your custom endpoint:
```bash
curl localhost:8080
```
The expected output is:
```
Greetings from Spring Boot!
```

### Layered Application

#### Configure the Base Layer

Next, create a base layer that contains `java.base`.
For this, use the following build in the _pom.xml_ file:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.graalvm.buildtools</groupId>
            <artifactId>native-maven-plugin</artifactId>
            <version>${native.maven.plugin.version}</version>
            <executions>
                <execution>
                    <goals>
                        <goal>build</goal>
                    </goals>
                    <phase>package</phase>
                </execution>
            </executions>
            <configuration>
                <imageName>libjavabaselayer</imageName>
                <buildArgs>
                    <buildArg>-cp ${project.basedir}/base_layer_config</buildArg>
                </buildArgs>
            </configuration>
        </plugin>
    </plugins>
</build>

```

with the following _native-image.properties_ file in _base_layer_config/META-INF/native-image/spring-base-layer_:
```
Args = -H:+UnlockExperimentalVMOptions \
       -H:LayerCreate=@layer-create.args \
       -H:-UnlockExperimentalVMOptions
```

Create the following _layer-create.args_ file in the same directory:
```
# base layer config that contains JDK modules used by Spring
base-layer.nil
digest-ignore
module=java.base
module=java.desktop
module=java.net.http
module=java.management
module=java.sql
module=jdk.unsupported
```

This is an alternate way to use the `-H:LayerCreate=` option. It is used to specify what should be included in the base layer: `java.base` and a few more other packages that a Spring application usually depends on.
For more details, consult the [Native Image Layers documentation](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/imagelayer/NativeImageLayers.md).

Now you can build the base layer:
```bash
../mvnw clean install
```
This will create the `base-layer.nil` which is a build time dependency for the application build.
It will also create the `libjavabaselayer.so` shared library which is a run time dependency for the application layer.
Note also that you use `install` instead of `package` to ensure that the base layer JAR is installed in the `.m2` cache as it will be needed by the application build later.

### Configure The Application Layer

To configure the application layer, add an additional profile:
```xml
<profile>
    <id>app-layer</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
                <version>${native.maven.plugin.version}</version>
                <configuration>
                    <imageName>spring-demo-layered</imageName>
                    <mainClass>com.example.demo.DemoApplication</mainClass>
                    <buildArgs combine.children="append">
						<buildArg>--verbose</buildArg>
                        <buildArg>-H:+UnlockExperimentalVMOptions</buildArg>
                        <buildArg>-H:LayerUse=../base-layer-test/target/base-layer.nil</buildArg>
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
../mvnw clean package -Dpackaging=native-image -Pnative -Papp-layer
```

This will generate the layered executable file in _./target/spring-demo-layered_ and will copy `libjavabaselayer.so` next to it.

Then you can execute the layered application:
```bash
./target/spring-demo-layered

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.0.2)
```
Test it with:
```
curl localhost:8080
```
The expected output is:
```
Greetings from Spring Boot!
```

### Learn More

* [Native Image Layers](https://github.com/oracle/graal/blob/master/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/imagelayer/NativeImageLayers.md)