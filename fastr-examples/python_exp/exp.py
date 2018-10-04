from random import random
from math import log
import sys

# Generate random numbers from exponential distribution with parameter rate
rate = 3
result = []
for i in range(1000):
    result += [-1/rate * log(random())]

# How can we check that the values are indeed from exponential distribution?
# We can use Kolmogorov–Smirnov test, which is implemented in the base R library:

import polyglot

# Create an R function and save it to python variable
kstest = polyglot.eval(string="function(x, lambda) ks.test(as.vector(x), 'pexp', lambda)$p.value", language="R")

# invoke the R function with arguments that are Python list and number and print the result
pvalue = kstest(result, rate)
print("The p-value of the test is %f" % pvalue)

# We can also visualize the data using the R lattice package

# Load the library in R and open a window for interactive R graphics 
polyglot.eval(string="library(lattice); awt()", language="R")

# Create R function that draws a histogram and save it to Python variable
plot = polyglot.eval(string="function(y) {print(histogram(~as.vector(y), main='Hello from Python to R', xlab='Python List')) }", language="R")

# invoke the R function from Python
plot(result)

# Get R function from file and immediately call it with the Python list. 
# The function draws similar plot as the previous one, but using ggplot2 package.
# Note that you need to install ggplot2 before running this example using "install.packages('ggplot2')"

with open("./ggplot2_example.R") as file:
    code = file.read()

polyglot.eval(string=code, language="R")(result, rate)
