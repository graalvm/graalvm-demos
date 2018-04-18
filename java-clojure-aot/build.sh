#!/usr/bin/env zsh

# generate jar
lein with-profile uberjar uberjar

# generate native image
$GRAALVM_HOME/bin/native-image -cp ./target/main.jar -H:Class=main.core
