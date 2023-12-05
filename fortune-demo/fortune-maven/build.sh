#!/bin/bash

# Exit on error
set -e

mvn clean compile
# Run the agent
mvn -Pnative -Dagent exec:exec@java-agent
# Build the native executable
mvn -Pnative -Dagent package
# Run the application with Maven and from a native executable
mvn -Pnative exec:exec@native
./target/fortune