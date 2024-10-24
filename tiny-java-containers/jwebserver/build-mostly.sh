#!/bin/sh

# Distroless Base (provides glibc)
docker build . -f Dockerfile.distroless-base.mostly -t jwebserver:distroless-base.mostly
