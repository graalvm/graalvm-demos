# Import the tempInCity function exported from the Ruby module
tempInCity <- import('tempInCity')

# The lattice library is needed for the visualization
library(lattice)

createModel <- function(size, length, getName, getLat, getLong, getTemp) {
  idx <- sample(1:length, size)
  data <- as.data.frame(list(
      name = sapply(idx, function(i) getName(i)),
      lat = sapply(idx, function(i) getLat(i)),
      long = sapply(idx, function(i) getLong(i)),
      temp = sapply(idx, function(i) getTemp(i))))
  list(data=data, model=lm(temp~lat, data=data))
}

do_predict <- function(model, lat) {
  predict(model$model, as.data.frame(list(lat = lat)))[[1]]
}

plotModel <- function(model) {
  svg()
  print(xyplot(temp ~ lat, data = model$data,
    panel = function(x, y) {
      panel.xyplot(x, y, cex=2, pch=19)
      panel.abline(model$model)
      labelsIdx <- seq(1, length(x), length.out = 10) # show only 10 labels, to make the graph more readable
      panel.text(x[labelsIdx] + 1, y[labelsIdx], model$data$name[labelsIdx], adj = c(0, 0.5))
  }));
  grDevices:::svg.off()
}

# Export the functions
export('createModel', createModel)
export('do_predict', do_predict)
export('plotModel', plotModel)
