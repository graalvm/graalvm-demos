# Compile the Java class that we will use in the benchmark
# Note: you need to have $JAVA_HOME variable defined in the environment
source("java.R")

# rJava initialization
library(rJava)
.jinit()
.jaddClassPath(getwd())

# the object that we are going to use for the benchmark
testObject <- .jnew("RJavaBench")

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
for (i in 1:5) {
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
