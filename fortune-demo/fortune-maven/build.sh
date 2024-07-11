#!/bin/bash

# Exit on error
set -e

# Build the native executable
mvn -Pnative package
# Run the application
./target/fortune