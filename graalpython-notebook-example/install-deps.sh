#!/bin/bash

set -e
set -u
set -o pipefail

graalpython -Im ensurepip --upgrade --default-pip
graalpython -m venv venv
VIRTUAL_ENV=${PWD}/venv
venv/bin/graalpython -m ginstall install matplotlib,Pillow,pandas