# This is the same benchmark as r_java_bench.R but using the built-in interop features of FastR, 
# which is much faster than the rJava emulation

if (!any(R.version$engine == "FastR")) {
    cat("This script runs only in FastR, not in GNU-R\n")
    exit(1)
}

# Compile the Java class that we will use in the benchmark
# Note: you need to have $JAVA_HOME variable defined in the environment
source("java.R")

# the object that we are going to use for the benchmark,
# syntactically this is the only difference from the rJava example
testObject <- new("RJavaBench")

benchmark <- function(obj) {
    result <- 0L
    for (j in 1:100) {
       obj2 <- obj$objectFunction(obj)
       obj$intField <- as.integer(obj2$doubleField)
       for (i in 1:250) {
           result <- obj$intFunction(i, obj$intField)
       }
    }
    result
}

result <- 0L
for (i in 1:10) {
  cat("iteration ", i, "\n")
  print(system.time(r <- benchmark(testObject)))

  # we do some artificial computation with the result so that
  # the compiler cannnot possibly ignore the call to benchmark() altogether
  result <- result + if (r > 0) 1 else -1;
}

cat("The computed number is ", result, "\n");

# cleanup
unlink("RJavaBench.java")
unlink("RJavaBench.class")
