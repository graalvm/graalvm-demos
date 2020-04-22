#!/bin/sh

RESULTS_DIR=${LOADTESTS_RESULTS:-$(pwd)}
DATE_SUFFIX=`date "+%Y-%m-%d-%H.%M"`
OUT_DIR=${RESULTS_DIR}/results-$DATE_SUFFIX
DURATION=${LOADTESTS_DURATION:-60}
CONC_REQS=${LOADTESTS_CONC_REQS:-2}

mkdir -p $OUT_DIR

set -x
jmeter -n -t LoadTest.jmx -J concReqs=$CONC_REQS -J duration=$DURATION \
        -J apiHostService=$TODOSERVICE_HOST -J apiHostPort=$TODOSERVICE_PORT \
         -l $OUT_DIR/loadTestResults.jtl
set +x
if [[ $? -ne 0 ]]; then
  echo "Error the jmeter command did not succeed"
  exit -1
fi

echo "Load test finished. Creating results"

jmeter -g $OUT_DIR/loadTestResults.jtl -o $OUT_DIR/report
if [[ $? -ne 0 ]]; then
  echo "Error creating the report for results. Try re-running the test"
  exit -1
fi

echo "Packaging results"
cp jmeter.log $OUT_DIR && cd $OUT_DIR && tar -czf results-$DATE_SUFFIX.tar.gz *

echo "Results generated in $OUT_DIR/report/index.html. Open the file to see the results(Ctrl+C to end test if running thru docker compose)"


