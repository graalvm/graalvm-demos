
# A simple, straightforward implementation of "Conway's Game of Life"
# lukas.stadler@oracle.com
#
# Start with "Rscript gameoflife.R"

cat("Conway's Game of Life\n")

step <- function(m) {
    result <- matrix(F, nrow(m), ncol(m))
    for (i in 1:nrow(m)) {
        for (j in 1:ncol(m)) {
            alive <- aliveNeighbors(m, i, j)
            if (alive > 3 || alive < 2) {
                result[[i,j]] <- FALSE
            } else if (alive == 2) {
                result[[i,j]] <- TRUE
            } else {
                result[[i,j]] <- m[[i,j]]
            }
        }
    }
    result
}

aliveNeighbors <- function(m, i, j) {
    cnt <- 0
    if (i > 1) cnt <- cnt + m[[i-1,j]]
    if (j > 1) cnt <- cnt + m[[i,j-1]]
    if (i < nrow(m)) cnt <- cnt + m[[i+1,j]]
    if (j < ncol(m)) cnt <- cnt + m[[i,j+1]]
    cnt
}

printMatrix <- function(m) {
    for (i in 1:nrow(m)) {
        for (j in 1:ncol(m))
            cat(if (m[[i,j]]) '[]' else '  ')
        cat("\n")
    }
}

size <- 50
m <- matrix(F, size, size)

set.seed(123)

for (i in 1:(size * size / 5)) {
    m[[runif(1)*nrow(m)+1,runif(1)*ncol(m)+1]] <- TRUE
}

for (i in 1:10) {
    printMatrix(m)
    cat("calculating 1000 iterations of a", size, "x", size," game of life\n")
    print(system.time(for (i in 1:1000) m <- step(m)))
}

q(status = (sum(m) != 113))
