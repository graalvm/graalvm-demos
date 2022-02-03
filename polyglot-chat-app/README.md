### A polyglot chat app

Download a GraalVM (tested with 22.0) and set the $JAVA_HOME environment variable to point to it.

Make sure to install Python and R:

    $JAVA_HOME/bin/gu install python R

Run the application:

    ./gradlew

Navigate to http://localhost:8080

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
