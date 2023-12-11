#!/bin/bash

set -e
set -u
set -o pipefail

 
source venv/bin/activate
VIRTUAL_ENV=${PWD}/venv
venv/bin/python -m ginstall install matplotlib Pillow pandas