# Builder
FROM container-registry.oracle.com/graalvm/native-image:25 AS nativebuild
WORKDIR /build
COPY . .
RUN chmod +x mvnw
RUN ./mvnw --no-transfer-progress package -Dpackaging=native-image

# Runner: Distroless Java Base - provides glibc and other libraries required by Native Image
FROM gcr.io/distroless/java-base-debian13
COPY --from=nativebuild /build/target/hello /
EXPOSE 8080
ENTRYPOINT ["/hello", "-b", "0.0.0.0", "-d", "/hello"]