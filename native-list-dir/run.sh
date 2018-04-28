#!/usr/bin/env bash
set -ex

time java ListDir $1
time ./listdir $1
