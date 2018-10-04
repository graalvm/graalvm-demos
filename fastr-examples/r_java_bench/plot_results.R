library(ggplot2)
results <- data.frame(
    name = c(rep('rJava & GNU-R', 5), rep('rJava emulation in FastR', 5), rep('FastR Java interop', 5)),
    iteration = c(1:5, 1:5, 1:5),
    time = c(46.48, 44.38, 45.88, 44.03, 43.26,
             42.62, 3.50, 3.63, 3.14, 3.02, 
             0.97, 0.26, 0.17, 0.22, 0.15))
png("results.png", width=480, height=300)
print(ggplot(aes(x = iteration, y = time, color = name), data = results) + 
 geom_point() + 
 geom_line() + 
 ggtitle("Using Java from R") + 
 ylab("time (seconds)\nlog2 scale") + 
 scale_y_continuous(trans='log2'))
dev.off()
