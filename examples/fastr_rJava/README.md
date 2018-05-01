# A small benchmark script exercising rJava functionality"

## Setup

Download the latest GraalVM image from the [Oracle Technology Network](http://www.oracle.com/technetwork/oracle-labs/program-languages/downloads/index.html).
Extract the archive and set the `GRAALVM_DIR` environment variable to point to the graalvm directory.

rJava needs to be installed, for FastR, from https://github.com/oracle/fastr/tree/master/com.oracle.truffle.r.pkgs/rJava

## Run

Execute the `run.sh` script or `${GRAALVM_DIR}/bin/Rscript rJavaBench.R`
