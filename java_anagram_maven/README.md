
Uses https://graalvm.github.io/native-build-tools/latest/maven-plugin-quickstart.html

Words taken from https://github.com/dwyl/english-words


## 1. Build and Run the Java Application

1. Compile the project on the JVM to create a runnable JAR with all dependencies. 
Open a terminal window and, from the root application directory, run:

```bash
mvn clean package
```

2. Run the Java application

```bash
java -jar target/java_anagram_maven-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## 2. Build and Run the Native Executable 

1. Run your application with the agent enabled:

```bash
mvn -Pnative -Dagent exec:exec@java-agent
```

The agent collects the metadata and generates the configuration files in a subdirectory of _target/native/agent-output_. Those files will be automatically used by the native-image tool if you pass the appropriate options.


2. Now build a native executable with the Maven profile:

```bash
mvn -DskipTests=true -Pnative -Dagent package
```

When the command completes a native executable, `AnagramSolver`, is created in the _target_ directory of the project and ready for use.

3. Run the native executable

```bash
./target/AnagramSolver
```

## 3. Build and Run the Native Executable with Static Initializer

```bash
mvn -Pnative-static -Dagent package
```

```bash
./target/AnagramSolver
```
