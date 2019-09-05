FROM oracle/graalvm-ce:19.2.0 as graalvm
COPY . /home/app/todo-service
WORKDIR /home/app/todo-service
RUN gu install native-image
RUN native-image --enable-all-security-services --no-server -cp 'build/libs/*'

FROM frolvlad/alpine-glibc
EXPOSE 8443
RUN mkdir /tmp/ssl-libs
COPY --from=graalvm /opt/graalvm-ce-19.2.0/jre/lib/amd64/libsunec.so .
COPY --from=graalvm /opt/graalvm-ce-19.2.0/jre/lib/security/cacerts /tmp/ssl-libs
COPY --from=graalvm /home/app/todo-service .
CMD ["./todo-service", "-Djavax.net.ssl.trustStore=/tmp/ssl-libs/cacerts"]
