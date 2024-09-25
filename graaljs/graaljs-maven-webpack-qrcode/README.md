# Using Node Packages in a Java Application

JavaScript libraries can be packaged with plain Java applications.
The integration is facilitated through [GraalJS Maven artifacts](https://central.sonatype.com/artifact/org.graalvm.polyglot/js) and the [GraalVM Polyglot API](https://www.graalvm.org/latest/reference-manual/embed-languages/), supporting a wide range of project setups.

Using Node (NPM) packages in Java projects often requires a bit more setup, due to the nature of the Node packaging ecosystem.
One way to use such modules is prepackage them into a single _.js_ or _.mjs_ file using a bundler like [webpack](https://webpack.js.org/).
This guide explains step-by-step how to integrate the `webpack` build into a Maven Java project and embed the generated JavaScript code in the JAR file of the application.

# GraalJS QRCode Demo

## 1. Getting Started

In this guide, you will add the [qrcode](https://www.npmjs.com/package/qrcode) NPM package to a Java application to generate QR codes.

To complete this guide, you need the following:

 * Some time on your hands
 * A decent text editor or IDE
 * JDK 21 or later
 * Maven 3.6.3 or later

We recommend that you follow the instructions in the next sections and create the application step by step.
However, you can go right to the completed example.

## 2. Setting Up the Maven Project

You can start with any Maven application that runs on JDK 21 or newer.
To follow this guide, generating the application from the [Maven Quickstart Archetype](https://maven.apache.org/archetypes/maven-archetype-quickstart/) is sufficient:

```shell
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DgroupId=com.example -DartifactId=qrdemo -DinteractiveMode=false
cd qrdemo
```

### 2.1. Adding the GraalJS Dependencies

Add the required dependencies for GraalJS in the `<dependencies>` section of the POM.

`pom.xml`
```xml
<!-- <dependencies> -->
<dependency>
    <groupId>org.graalvm.polyglot</groupId>
    <artifactId>polyglot</artifactId> <!-- ① -->
    <version>24.1.0</version>
</dependency>

<dependency>
    <groupId>org.graalvm.polyglot</groupId>
    <artifactId>js</artifactId> <!-- ② -->
    <version>24.1.0</version>
    <type>pom</type> <!-- ③ -->
</dependency>
<!-- </dependencies> -->
```

❶ The `polyglot` dependency provides the APIs to manage and use GraalJS from Java.

❷ The `js` dependency is a meta-package that transitively depends on all libraries and resources to run GraalJS.

❸ Note that the `js` package is not a JAR - it is simply a POM that declares more dependencies.

### 2.2. Adding the Maven Frontend Plugin

Most JavaScript packages are hosted on a package registry like [NPM](https://www.npmjs.com/) or [JSR](https://jsr.io/) and can be installed using a package manager such as `npm`.
The Node.js ecosystem has conventions about the filesystem layout of installed packages that need to be kept in mind when embedding into Java.
To simplify the integration, a bundler can be used to repackage all dependencies in a single file.
You can use the [`frontend-maven-plugin`](https://github.com/eirslett/frontend-maven-plugin) to manage the download, installation, and bundling for you.

`pom.xml`
```xml
<!-- <build> -->
<plugins>
    <plugin>
        <groupId>com.github.eirslett</groupId>
        <artifactId>frontend-maven-plugin</artifactId>
        <version>1.15.0</version>

        <configuration>
            <nodeVersion>v21.7.2</nodeVersion>
            <workingDirectory>src/main/js</workingDirectory>
            <installDirectory>target</installDirectory>
        </configuration>

        <executions>
            <execution>
                <!-- ① -->
                <id>install node and npm</id>
                <goals><goal>install-node-and-npm</goal></goals>
            </execution>

            <execution>
                <!-- ② -->
                <id>npm install</id>
                <goals><goal>npm</goal></goals>
            </execution>

            <execution>
                <!-- ③ -->
                <id>webpack build</id>
                <goals><goal>webpack</goal></goals>
                <configuration>
                    <arguments>--mode production</arguments>
                    <environmentVariables>
                        <BUILD_DIR>${project.build.outputDirectory}/bundle</BUILD_DIR>
                    </environmentVariables>
                </configuration>
            </execution>
        </executions>
    </plugin>
</plugins>
<!-- </build> -->
```

❶ Installs `node` and `npm`.

❷ Runs `npm install` to download and install all NPM packages in _src/main/js_.

❸ Runs `webpack` to build a bundle of the JS sources in _target/classes/bundle_, which will be later included in the application's JAR file and can be loaded as a resource.

## 3. Setting Up the JavaScript Build.

```shell
mkdir src/main/js
cd src/main/js
```

Manual steps to set up the build environment:
1. Run `npm init` and follow the instructions (package name: "qrdemo", entry point: "main.mjs").
2. Run `npm install -D @webpack-cli/generators`.
3. Run `npx webpack-cli init` and follow the instructions to set up a webpack project (select "ES6" and "npm").
4. Run `npm install --save qrcode` to install and add the `qrcode` dependency.
5. Run `npm install --save assert util stream-browserify browserify-zlib fast-text-encoding` to install the polyfill packages need to build with the webpack configuration below.

Alternatively, create a _package.json_ file with the following contents:
```js
{
  "name": "qrdemo",
  "version": "1.0.0",
  "description": "QRCode demo app",
  "main": "main.mjs",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1",
    "build": "webpack --mode=production --node-env=production",
    "build:dev": "webpack --mode=development",
    "build:prod": "webpack --mode=production --node-env=production",
    "watch": "webpack --watch"
  },
  "author": "",
  "license": "ISC",
  "dependencies": {
    "assert": "^2.1.0",
    "browserify-zlib": "^0.2.0",
    "fast-text-encoding": "^1.0.6",
    "qrcode": "^1.5.4",
    "stream-browserify": "^3.0.0",
    "util": "^0.12.5"
  },
  "devDependencies": {
    "@babel/core": "^7.25.2",
    "@babel/preset-env": "^7.25.4",
    "@webpack-cli/generators": "^3.0.7",
    "babel-loader": "^9.1.3",
    "webpack": "^5.94.0",
    "webpack-cli": "^5.1.4"
  }
}
```

Create a _webpack.config.js_ file, or open the one created by `webpack-cli init`, and fill it with the following contents:

```js
const path = require('path');
const { EnvironmentPlugin } = require('webpack');

const config = {
    entry: './main.mjs',
    output: {
        path: path.resolve(process.env.BUILD_DIR),
        filename: 'bundle.mjs',
        module: true,
        library: {
            type: 'module',
        },
        globalObject: 'globalThis'
    },
    experiments: {
        outputModule: true // Generate ES module sources
    },
    optimization: {
        usedExports: true, // Include only used exports in the bundle
        minimize: false,   // Disable minification
    },
    resolve: {
        aliasFields: [],   // Disable browser alias to use the server version of the qrcode package
        fallback: {        // Redirect Node.js core modules to polyfills
            "stream": require.resolve("stream-browserify"),
            "zlib": require.resolve("browserify-zlib"),
            "fs": false    // Exclude the fs module altogether
        },
    },
    plugins: [
        new EnvironmentPlugin({
            NODE_DEBUG: false, // Set process.env.NODE_DEBUG to false
        }),
    ],
};

module.exports = () => config;
```

Create `main.mjs`, the entry point of the bundle, with the following contents:
```js
// GraalJS doesn't have built-in TextEncoder support yet. It's easy to import it from a polyfill in the meantime.
import 'fast-text-encoding';

// Re-export the "qrcode" module as a "QRCode" object in the exports of the bundle.
export * as QRCode from 'qrcode';
```

## 4. Using the JavaScript Library from Java

After reading the [qrcode](https://www.npmjs.com/package/qrcode) docs, you can write Java interfaces that match the [JavaScript types](https://www.npmjs.com/package/@types/qrcode) you want to use and methods you want to call on them.
GraalJS makes it easy to access JavaScript objects via these interfaces.
Java method names are mapped directly to JavaScript function and method names.
The names of the interfaces can be chosen freely, but it makes sense to base them on the JavaScript types.

_src/main/java/com/example/QRCode.java_
```java
package com.example;

interface QRCode {
    Promise toString(String data);
}
```

_src/main/java/com/example/Promise.java_
```java
package com.example;

public interface Promise {
    Promise then(ValueConsumer onResolve);

    Promise then(ValueConsumer onResolve, ValueConsumer onReject);
}
```

_src/main/java/com/example/ValueConsumer.java_
```java
package com.example;

import java.util.function.*;
import org.graalvm.polyglot.*;

@FunctionalInterface
public interface ValueConsumer extends Consumer<Value> {
    @Override
    void accept(Value value);
}
```

Using the `Context` class and these interfaces, you can now create QR codes and convert them to a Unicode string representation or an image.
Our example just prints the QR code to `stdout`.

_src/main/java/com/example/App.java_
```java
package com.example;

import org.graalvm.polyglot.*;

public class App {
    public static void main(String[] args) throws Exception {
        try (Context context = Context.newBuilder("js")
                    .allowHostAccess(HostAccess.ALL)
                    .option("engine.WarnInterpreterOnly", "false")
                    .option("js.esm-eval-returns-exports", "true")
                    .option("js.unhandled-rejections", "throw")
                    .build()) {
            Source bundleSrc = Source.newBuilder("js", App.class.getResource("/bundle/bundle.mjs")).build(); // ①
            Value exports = context.eval(bundleSrc);
            QRCode qrCode = exports.getMember("QRCode").as(QRCode.class); // ②
            String input = args.length > 0 ? args[0] : "https://www.graalvm.org/javascript/";
            Promise resultPromise = qrCode.toString(input); // ③
            resultPromise.then( // ④
                (Value output) -> {
                    System.out.println("Successfully generated QR code for \"" + input + "\".");
                    System.out.println(output.asString());
                }
            );
        }
    }
}
```

❶ Load the bundle generated by `webpack` from a resource embedded in the JAR file.

❷ JavaScript objects are returned using a generic [Value](https://www.graalvm.org/truffle/javadoc/org/graalvm/polyglot/Value.html) type.
You can cast the exported `QRCode` object to the declared `QRCode` interface so that you can use Java typing and IDE completion features.

❸ `QRCode.toString` does not return the result directly but as a `Promise<string>` (alternatively, it can also be used with a callback).

❹ Invoke the `then` method of the `Promise` to eventually obtain the QRCode string and print it to `stdout`.

## 5. Running the Application

If you followed along with the example, you can now compile and run your application from the command line:

```shell
mvn package
mvn exec:java -Dexec.mainClass=com.example.App -Dexec.args="https://www.graalvm.org/"
```

The expected output should be similar to this:
```
Successfully generated QR code for "https://www.graalvm.org/".


    █▀▀▀▀▀█  ▀▄ ▀▄█▄▀ █▀▀▀▀▀█
    █ ███ █ █▄ ▄ ▄▄▀▀ █ ███ █
    █ ▀▀▀ █ █  ▄▀▀▄▄█ █ ▀▀▀ █
    ▀▀▀▀▀▀▀ █ █▄▀ █▄▀ ▀▀▀▀▀▀▀
    █ ▀▀▀█▀▄ ▄█▀ █ ▀▄▄▀█▀▀▀▄
    ██▄  ▀▀▄ ▀▄▄█▀▀█▀█▀█▀▀ ▀█
    ██▀▀█▄▀█▄▄  ▄█▀▀▄█▀█▀▄▀█▀
    █ ▄█▄▀▀  ▀▀ ▄▀█▀ █▀██▀ ▀█
    ▀  ▀ ▀▀ ██▄ ▀▀█▀█▀▀▀█▄▀
    █▀▀▀▀▀█ ▄ ▄█▀▀  █ ▀ █▄▀▀█
    █ ███ █ ███▀█▀▀▀█▀█▀█▄█▄▄
    █ ▀▀▀ █ ▀▄▄▄ ▀█▄▄▄ ▄▄█▀ █
    ▀▀▀▀▀▀▀ ▀ ▀▀▀▀     ▀▀▀▀▀▀
```

## 6. Conclusion

By following this guide, you've learned how to:
* Use GraalJS and the GraalVM Polyglot API to embed a JavaScript library in your Java application.
* Use Webpack to bundle an NPM package into a self-contained _.mjs_ file, including its dependencies and polyfills for Node.js core modules that may be required to run on GraalJS.
* Use the Maven Frontend Plugin to seamlessly integrate the `npm install` and `webpack` build steps into your Maven project.

Feel free to use this demo as inspiration and a starting point for your own applications!
