#!/bin/bash

set -e
set -u
set -o pipefail

$GRAALVM_HOME/bin/graalpy -Im ensurepip --upgrade --default-pip
$GRAALVM_HOME/bin/graalpy -m venv venv
VIRTUAL_ENV=${PWD}/venv
venv/bin/graalpy -m ginstall install matplotlib,Pillow,pandas