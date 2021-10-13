#!/bin/bash

set -e
set -u
set -o pipefail

graalpython -m venv venv
VIRTUAL_ENV=venv
venv/bin/graalpython -m ginstall install matplotlib,Pillow,pandas