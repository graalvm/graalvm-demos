#!/bin/bash

# Exit on error
set -e

# GRAALVM_HOME="/home/mukel/Desktop/graal/graal/sdk/mxbuild/linux-amd64/GRAALVM_C152252C1A_JAVA11/graalvm-c152252c1a-java11-21.0.0-dev"
SCRATCH=./out

if [[ -z "$GRAALVM_HOME" ]]; then
    echo "GRAALVM_HOME is not set"
    exit 1
fi

echo $GRAALVM_HOME

# Manually build espresso-jshell.jar, requires GraalVM >= 21.0 with Native Image and Java on Truffle (Espresso) installed.
echo "1/7 Compiling espresso-jshell..."
$GRAALVM_HOME/bin/javac $(find src/main/java -name '*.java') -d "$SCRATCH"

echo "2/7 Packaging espresso-jshell.jar..."
cp -r ./src/main/resources/* "$SCRATCH"
jar cef com.oracle.truffle.espresso.jshell.JavaShellLauncher espresso-jshell.jar -C "$SCRATCH" .

echo "3/7 Cleaning up scratch folder..."
rm -r "$SCRATCH"

echo "4/7 Compiling jshell8..."
mkdir -p "$SCRATCH"
$GRAALVM_HOME/bin/javac -source 8 -target 8 $(find jshell8 -name '*.java') -d "$SCRATCH"

echo "5/7 Packaging jshell8.jar..."
jar cf jshell8.jar -C "$SCRATCH" .

echo "6/7 Cleaning up scratch folder..."
rm -r "$SCRATCH"

echo "7/7 Building espresso-jshell native image, this may take a few minutes..."
"$GRAALVM_HOME/bin/native-image" \
  -H:+AllowJRTFileSystem \
  -H:Name=espresso-jshell \
  --initialize-at-build-time=com.sun.tools.doclint,'jdk.jshell.Snippet$SubKind' \
  -H:ConfigurationFileDirectories=. \
  --language:java \
  -J-Djdk.image.use.jvm.map=false \
  -J-Dorg.graalvm.launcher.home="$GRAALVM_HOME" \
  -J--patch-module -Jjdk.jshell=./jdk.jshell-patch `# patch for Java 8, not required for 11` \
  -H:+ReportExceptionStackTraces \
  $@ \
  -jar espresso-jshell.jar

echo "Done!"
