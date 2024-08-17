# Using Oracle GraalVM for JDK 17
FROM container-registry.oracle.com/graalvm/native-image:21-ol8 AS builder

# gzip required for maven wrapper builds
RUN microdnf install gzip

# findutils needed for gradle wrapper build
RUN microdnf install findutils
# Set the working directory to /home/app
WORKDIR /build

# Copy the source code into the image for building
COPY . /build

# Build
RUN ./mvnw --no-transfer-progress native:compile -Pnative

# The deployment Image
FROM container-registry.oracle.com/os/oraclelinux:8-slim

EXPOSE 8080

# Copy the native executable into the containers
COPY --from=builder /build/target/benchmark-jibber app
ENTRYPOINT ["/app"]
