# A small benchmark script exercising rJava functionality.
# lukas.stadler@oracle.com
#
# Start with Rscript rJavaBench.R
# (rJava needs to be installed, e.g., from https://github.com/oracle/fastr/tree/master/com.oracle.truffle.r.pkgs/rJava)

javaSource <-"
public class RJavaBench {

  public int intField;
  public double doubleField;
  
  public int intFunction(int a, int b) {
    return a + b;
  }
  
  public RJavaBench objectFunction(RJavaBench a) {
    RJavaBench result = new RJavaBench();
    result.intField = (intField + a.intField + 1) / 2;
    result.doubleField = (doubleField + a.doubleField) / 2;
    return result;
  }
}
"
writeLines(javaSource, "RJavaBench.java")
system("javac RJavaBench.java")

library(rJava)

callIntFunction <- function(size, x) {
  s <- 1L
  for (i in 1:size) {
    s <- x$intFunction(s, 1L)
  }
  s
}

callObjectFunction <- function(size, x) {
  current <- x
  for (i in 1:size) {
    current <- current$objectFunction(current)
  }
}

readWriteFields <- function(size, x) {
  for (i in 1:size) {
    x$intField <- x$intField + 1L
    x$doubleField <- x$doubleField + 1
  }
}

.jinit()
.jaddClassPath(getwd())
testObject <- .jnew("RJavaBench")

cat("callIntFunction:\n")
for (i in 1:5) print(system.time(callIntFunction(10000, testObject)))

cat("callObjectFunction:\n")
for (i in 1:5) print(system.time(callObjectFunction(10000, testObject)))

cat("readWriteFields:\n")
for (i in 1:5) print(system.time(readWriteFields(10000, testObject)))
