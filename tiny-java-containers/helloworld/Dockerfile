
# Build in a container with Oracle GraalVM Native Image and MUSL
FROM container-registry.oracle.com/graalvm/native-image:23-muslib AS nativebuild
WORKDIR /build
# Install UPX 
ARG UPX_VERSION=4.2.2
ARG UPX_ARCHIVE=upx-${UPX_VERSION}-amd64_linux.tar.xz
RUN microdnf -y install wget xz && \
    wget -q https://github.com/upx/upx/releases/download/v${UPX_VERSION}/${UPX_ARCHIVE} && \
    tar -xJf ${UPX_ARCHIVE} && \
    rm -rf ${UPX_ARCHIVE} && \
    mv upx-${UPX_VERSION}-amd64_linux/upx . && \
    rm -rf upx-${UPX_VERSION}-amd64_linux

# Compile the Hello class to Java bytecode
COPY Hello.java Hello.java
RUN javac Hello.java
# Build a native executable with native-image
RUN native-image -Os --static --libc=musl Hello -o hello
RUN ls -lh hello

# Compress the executable with UPX
RUN ./upx --lzma --best -o hello.upx hello
RUN ls -lh hello.upx

# Copy the compressed executable into a scratch container
FROM scratch
COPY --from=nativebuild /build/hello.upx /hello.upx
ENTRYPOINT ["/hello.upx"]
