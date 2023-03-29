
Uses https://graalvm.github.io/native-build-tools/latest/maven-plugin-quickstart.html

Words taken from https://github.com/dwyl/english-words


## 1. Build and Test the Java Application

```bash
./mvnw test
```

## 2. Build and Run the Java Application

1. Compile the project on the JVM to create a runnable JAR with all dependencies. 
Open a terminal window and, from the root application directory, run:

```bash
mvn clean package
```

2. Run the Java application

```bash
./mvnw mn:run
```


## 2. Build and Run the Native Executable 

1. Run your application with the agent enabled:

```bash
mvn -Pnative -Dagent exec:exec@java-agent
```

The agent collects the metadata and generates the configuration files in a subdirectory of _target/native/agent-output_. Those files will be automatically used by the native-image tool if you pass the appropriate options.


2. Now build a native executable:

```bash
./mvnw package -Dpackaging=native-image 
```

When the command completes a native executable, `Anagram_Server`, is created in the _target_ directory of the project and ready for use.

3. Run the native executable

```bash
./target/AnagramSolver
```



Time taken to create instance: 465
Tests run: 11565
Time taken: 56463

## 3. Use Profile-Guided Optimization to Improve Performance

1. Instrument the native executable

```bash
mvn -Ppgo-instrument -Dagent package
```

2. Run the application and provide some examples

```bash
./target/AnagramSolver
```

3. Build a native executable using the results of the instrumentation

```bash
mvn -Ppgo -Dagent package
```

4. Run the native executable -- it should start more quickly and produce anagrams more quickly


Time taken to create instance: 494
Tests run: 11565
Time taken: 65142

## 4. Build and Run the Native Executable with Static Initializer

1. Build a native executable using the results of the instrumentation and initialize the main class

```bash
mvn -Pstatic-pgo -Dagent package
```

2. Run the native executable -- it should start instantly and produce anagrams quickly

```bash
./target/AnagramSolver
```

Time taken to create instance: 0
Tests run: 11565
Time taken: 64952