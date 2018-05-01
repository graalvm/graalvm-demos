# FastR Node.js interop example

Demo inspired by Shiny R web applications framework [examples](https://shiny.rstudio.com/gallery/kmeans-example.html).


## Setup

Download the latest GraalVM image from the [Oracle Technology Network](http://www.oracle.com/technetwork/oracle-labs/program-languages/downloads/index.html).
Extract the archive and set the `GRAALVM_DIR` environment variable to point to the graalvm directory.

Then, execute the `build.sh` script.

A typical problem is proxy set-up: verify that you have the `http_proxy`, `https_proxy`, and `no_proxy` environment variables set properly.


## Run

Execute the `run.sh` script and open your browser at `http://localhost:12837`.
