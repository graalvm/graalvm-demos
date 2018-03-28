#!/usr/bin/env bash
set -ex

time java ListDir $1
time ./listDir $1
