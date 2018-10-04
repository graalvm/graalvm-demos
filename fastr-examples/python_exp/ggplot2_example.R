function(result, rate) {
  if (require(ggplot2)) {
    print(ggplot(data.frame(result=as.vector(result)), aes(x=result)) + 
      geom_histogram(aes(y=..density..), binwidth=.1) +
      stat_function(fun=function(x) dexp(x,rate), aes(color="theoretical")) +
      geom_density(alpha=.2, aes(color="actual")) +
      ggtitle("Hello from ggplot2 to Python") +
      scale_colour_manual("Density", values = c("red", "blue")))
  } else {
    cat("ggplot2 not installed.\n")
    cat("To run the ggplot2 visualization, install it using: R -e 'install.packages(\"ggplot2\")'\n");
    cat("Note: the installation may take up to 10 minutes\n");
  }
}
