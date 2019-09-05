#!/bin/sh
OUT_DIR=${LOADTESTS_RESULTS:-out/}

rm -rf $OUT_DIR
mkdir -p $OUT_DIR
jmeter -n -t LoadTest.jmx -J apiHostService=$TODOSERVICE_HOST -J apiHostPort=$TODOSERVICE_PORT -l $OUT_DIR/loadTestResults.jtl
if [ $? -ne 0 ]; then
  echo "Error the jmeter command did not succeed"
  exit -1
fi

echo "Load test finished. Creating results"

jmeter -g $OUT_DIR/loadTestResults.jtl -o $OUT_DIR/report
if [ $? -ne 0 ]; then
  echo "Error creating the report for results. Try re-running the test"
  exit -1
fi


echo "Results generated in $OUT_DIR/report/index.html. Open the file to see the results(Ctrl+C to end test if running thru docker compose)"


