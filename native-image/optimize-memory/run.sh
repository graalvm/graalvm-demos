#!/usr/bin/env bash
set -ex

# Compile and run the application on HotSpot
javac StringManipulation.java
/usr/bin/time java StringManipulation 500000 50000

# Build a Native Image with Serial GC (default)
native-image -Ob -o testgc-serial StringManipulation
/usr/bin/time ./testgc-serial 500000 50000 -XX:+PrintGC

# Build a Native Image with G1 GC
native-image -Ob --gc=G1 -o testgc-g1 StringManipulation
/usr/bin/time ./testgc-g1 500000 50000 -XX:+PrintGC

# Build a Native Image with Epsilon GC
native-image -Ob --gc=epsilon -o testgc-epsilon StringManipulation
/usr/bin/time ./testgc-epsilon 100000 50000

# Build a Native Image Setting the Maximum Heap Size
# At run time
/usr/bin/time ./testgc-g1 -Xmx512m 500000 50000
# At build Time
native-image -Ob --gc=G1 -R:MaxHeapSize=512m -o testgc-maxheapset-g1 StringManipulation
/usr/bin/time ./testgc-maxheapset-g1 500000 50000