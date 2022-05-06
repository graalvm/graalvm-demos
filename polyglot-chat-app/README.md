### Polyglot Chat Application

1. [Download GraalVM](https://www.graalvm.org/downloads/), unzip the archive, export the GraalVM home directory as the `$JAVA_HOME` and add `$JAVA_HOME/bin` to the `PATH` environment variable.

  On Linux:
  ```bash
  export JAVA_HOME=/home/${current_user}/path/to/graalvm
  export PATH=$JAVA_HOME/bin:$PATH
  ```
  On macOS:
  ```bash
  export JAVA_HOME=/Users/${current_user}/path/to/graalvm/Contents/Home
  export PATH=$JAVA_HOME/bin:$PATH
  ```
  On Windows:
  ```bash
  setx /M JAVA_HOME "C:\Progra~1\Java\<graalvm>"
  setx /M PATH "C:\Progra~1\Java\<graalvm>\bin;%PATH%"
  ``` 

2. Add Python and R support for your GraalVM installation:

    ```bash
    $JAVA_HOME/bin/gu install python R
    ```

3. Build and run the application:
    
    ```
    ./gradlew run
    ```

4. Navigate to http://localhost:8080

You can connect from multiple browsers and chat via websockets. You can use "/"
commands to trigger certain interesting functions. Available are:

    /img [some string]

Searches for a random image from the internet matching [some string].

    /gif [some string]

Searches for a GIF on the internet matching [some string].

    /= 1 2 3 4

Echos the arguments `1 2 3 4`.

    /plot 1 1.4 2.6 6.4 25.6 102.4

Plots the points given.
