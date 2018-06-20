#!/bin/sh

$GRAALVM_HOME/bin/native-image -cp $SCALA_HOME/lib/scala-compiler.jar:$SCALA_HOME/lib/scala-library.jar:$SCALA_HOME/lib/scala-reflect.jar:$PWD/scalac-substitutions/target/scala-2.12/scalac-substitutions_2.12-0.1.0-SNAPSHOT.jar -H:SubstitutionResources=substitutions.json,substitutions-2.12.json -H:ReflectionConfigurationFiles=scalac-substitutions/reflection-config.json -H:Class=scala.tools.nsc.Main -H:Name=scalac $@
