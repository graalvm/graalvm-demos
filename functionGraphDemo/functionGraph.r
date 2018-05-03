library(stats)
library(lattice)

plotFunction <- function(expr, x1, x2) {
  svg()
  x <- seq(from = x1, to = x2, by = 0.01)
  y <- eval(parse(text=expr))
  print(xyplot(y ~ x, type="l"))
  grDevices:::svg.off()
}

export('plotFunction', plotFunction)
