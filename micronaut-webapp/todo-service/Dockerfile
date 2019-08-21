FROM oracle/graalvm-ce:19.2.0 as graalvm
COPY . /home/app/todo-service
WORKDIR /home/app/todo-service
RUN gu install native-image
RUN native-image --no-server -cp 'build/libs/*'

FROM frolvlad/alpine-glibc
EXPOSE 8080
COPY --from=graalvm /home/app/todo-service .
ENTRYPOINT ["./todo-service"]
