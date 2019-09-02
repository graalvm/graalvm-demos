FROM oracle/graalvm-ce:19.2.0 as graalvm
COPY . /home/app/frontend
WORKDIR /home/app/frontend
RUN gu install native-image
RUN native-image --no-server -cp 'build/libs/*'

FROM frolvlad/alpine-glibc
EXPOSE 8081
COPY --from=graalvm /home/app/frontend .
ENTRYPOINT ["./frontend"]
