#!/bin/bash

# Exit on error
set -e

# Build the native executable
mvn -Pnative package
# Run the application with Maven and from a native executable
./target/fortune