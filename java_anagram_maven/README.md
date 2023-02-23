
1. Compile the project on the JVM to create a runnable JAR with all dependencies. 
Open a terminal window and, from the root application directory, run:

```bash
mvn clean package
```

2. Run your application with the agent enabled:

```
mvn -Pnative -Dagent exec:exec@java-agent
The agent collects the metadata and generates the configuration files in a subdirectory of target/native/agent-output. Those files will be automatically used by the native-image tool if you pass the appropriate options.
```

3. Now build a native executable with the Maven profile:
```
mvn -DskipTests=true -Pnative -Dagent package
```

When the command completes a native executable, fortune, is created in the /target directory of the project and ready for use.
