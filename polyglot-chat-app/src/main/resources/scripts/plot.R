library(lattice)

toPlot <- function(data) {
    y = scan(textConnection(data))
    x <- seq_along(y)
    svg('/dev/null', width=5, height=3)
    print(xyplot(y~x))
    return (svg.off())
}

# We are exporting the function so that other languages can access it
export('toPlot', toPlot)