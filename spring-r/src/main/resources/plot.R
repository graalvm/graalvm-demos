library(ggplot2)
data <<- numeric(100)

function(dataHolder) {
    svg()
    data <<- c(data[2:100], dataHolder$value)

    logHolder <- java.type("org.graalvm.demos.springr.LogHolder")
    logHolder$log(dataHolder$value, data[90:100])

    plot <- ggplot(data = data.frame(systemLoad = data, time = -99:0),
                aes(x=time, y=systemLoad, group=1)) +
                geom_line(color="orange") +
                expand_limits(x=0, y=0)
    print(plot)
    svg.off()
}
