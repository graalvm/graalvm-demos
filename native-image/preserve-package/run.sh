#!/usr/bin/env bash
set +e

#
# Java build
#
./mvnw clean package
$JAVA_HOME/bin/java -jar target/preserve-package-1.0-SNAPSHOT.jar \
       org.graalvm.example.action.StringReverser reverse "hello"

#
# Default Native Image Build (no preserve)
#
./mvnw package -Pnative-default
./target/example-default \
       org.graalvm.example.action.StringReverser reverse "hello"
# Verify failure by checking exit code <> 0
if [ $? -eq 0 ]; then 
   echo "Error: native executable should exit with error"
   exit 1
fi
# Verify Build Report generated
if [ ! -f target/example-default-build-report.html]; then 
   echo "Error: build report not found!"
   exit 1
fi

# 
# Native Image build with Preserve
#
./mvnw package -Pnative-preserve
./target/example-preserve \
       org.graalvm.example.action.StringReverser reverse "hello"
# Verify success by checking exit code = 1
if [ $? -ne 0 ]; then 
   echo "Error: native executable should exit without error"
   exit 1
fi