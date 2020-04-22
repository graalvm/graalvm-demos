FROM oracle/graalvm-ce:19.2.1 as graalvm
COPY . /home/app/frontend
WORKDIR /home/app/frontend
RUN gu install native-image
RUN native-image --verbose --no-server --enable-all-security-services -cp 'build/libs/*'

FROM frolvlad/alpine-glibc
EXPOSE 8081
COPY --from=graalvm /home/app/frontend .
COPY --from=graalvm /opt/graalvm-ce-19.2.1/jre/lib/amd64/libsunec.so .
COPY --from=graalvm /opt/graalvm-ce-19.2.1/jre/lib/security/cacerts /tmp/ssl-libs
ENTRYPOINT ["./frontend", "-Djava.library.path=."]
