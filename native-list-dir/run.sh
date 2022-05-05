#!/usr/bin/env bash
set -ex

time java ListDir $1
time ./listdir $1

time java ExtListDir $1
time ./extlistdir $1
