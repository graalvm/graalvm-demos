#!/bin/bash

set -e
set -u
set -o pipefail

$JAVA_HOME/bin/graalpy -Im ensurepip --upgrade --default-pip
$JAVA_HOME/bin/graalpy -m venv venv
VIRTUAL_ENV=${PWD}/venv
venv/bin/graalpy -m ginstall install matplotlib,Pillow,pandas