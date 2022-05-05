#!/bin/bash

set -e
set -u
set -o pipefail

$GRAALVM_HOME/bin/graalpython -Im ensurepip --upgrade --default-pip
$GRAALVM_HOME/bin/graalpython -m venv venv
VIRTUAL_ENV=${PWD}/venv
venv/bin/graalpython -m ginstall install matplotlib,Pillow,pandas