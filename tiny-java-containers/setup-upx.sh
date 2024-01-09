#!/bin/sh

set -e

UPX_VERSION=4.2.2
UPX_ARCHIVE=upx-${UPX_VERSION}-amd64_linux.tar.xz 

wget -q https://github.com/upx/upx/releases/download/v${UPX_VERSION}/${UPX_ARCHIVE}
tar -xJf ${UPX_ARCHIVE}
rm -rf ${UPX_ARCHIVE}
mv upx-${UPX_VERSION}-amd64_linux/upx .
rm -rf upx-${UPX_VERSION}-amd64_linux

