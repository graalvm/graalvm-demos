#!/bin/sh

rm -rf out/
jmeter -n -t LoadTest.jmx -J apiHostService=$TODOSERVICE_HOST -J apiHostPort=$TODOSERVICE_PORT -l loadTestResults.jtl
if [ $? -ne 0 ]; then
  echo "Error the jmeter command did not succeed"
  exit -1
fi

echo "Load test finished. Creating results"

jmeter -g loadTestResults.jtl -o out/
if [ $? -ne 0 ]; then
  echo "Error creating the report for results. Try re-running the test"
  exit -1
fi

echo "Results generated in out/index.html. Open the file to see the results(Ctrl+C to end test if running thru docker compose)"


