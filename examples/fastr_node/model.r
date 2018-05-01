library(lattice)

plotkmeans <- function(x, y, clusters) {
  svg(width=10, height=8)
  data <- kmeans(iris[, c(y, x)], as.integer(clusters))
  iris$cluster <- factor(data$cluster)
  print(xyplot(as.formula(paste0(y,'~',x)), data=iris, groups=cluster, pch=20, cex=3))
  grDevices:::svg.off()
}

plotcars <- function(x, y, z) {
  svg(width=10, height=8)
  mtcars$gear.f<-factor(mtcars$gear,levels=c(3,4,5), labels=c("3gears","4gears","5gears"))
  mtcars$cyl.f <-factor(mtcars$cyl,levels=c(4,6,8), labels=c("4cyl","6cyl","8cyl"))
  print(cloud(as.formula(paste0(x,'~',y,'*',z)), main="3D Scatterplot generated in R", data=mtcars))
  grDevices:::svg.off()
}

model <- lm(weight~height, data=women)

plotheightweitgh <- function(x, y, z) {
  svg(width=10, height=8)
  print(xyplot(weight ~ height, data = women,
    panel = function(x, y) {
      panel.xyplot(x, y, cex=2, pch=19)
      panel.abline(model)
  }));
  grDevices:::svg.off()
}

predictweight <- function(height) {
    predict(model, as.data.frame(list(height=height)))
}

export('plotkmeans', plotkmeans)
export('plotcars', plotcars)
export('plotheightweight', plotheightweitgh)
export('predictweight', predictweight)
