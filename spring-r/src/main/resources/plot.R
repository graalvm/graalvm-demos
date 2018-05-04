library(ggplot2)
data <<- numeric(100)

function(l) {
    svg()
    data <<- c(data[2:100], l)
    plot <- ggplot(data = data.frame(systemLoad = data, time = -99:0),
                aes(x=time, y=systemLoad, group=1)) +
                geom_line(color="red") +
                expand_limits(x=0, y=0)
    print(plot)
    grDevices:::svg.off()
}
