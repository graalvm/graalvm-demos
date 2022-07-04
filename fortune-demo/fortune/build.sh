#!/bin/bash

# Exit on error
set -e

mvn clean compile
# Running the agent
mvn -Pnative -Dagent exec:exec@java-agent
# Building the native executable
mvn -Pnative -Dagent package
# Running the application with Maven and as a native executable
mvn -Pnative exec:exec@native
./target/fortune