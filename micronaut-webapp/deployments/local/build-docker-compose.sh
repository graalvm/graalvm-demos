#!/bin/sh

dockerfile_flavor=
case "$1" in
    graalvm* )
    dockerfile_flavor=-graalvm
    ;;
    hotspot*)
    dockerfile_flavor=-hotspot
    ;;
esac

cat > docker-compose.tpl << "EOF"
version: '3'
services:
  todo-service:
    build:
      context: ./todo-service
      dockerfile: Dockerfile$flavor
    ports:
      - 8443:8443
  frontend:
    build: 
      context: ./frontend
      dockerfile: Dockerfile$flavor
    depends_on:
      - todo-service
    ports:
      - 8081:8081
    environment:
      - TODOSERVICE_URL=https://todo-service:8443
  loadtest:
    build:
      context: ./loadTests/
      dockerfile: Dockerfile
    depends_on:
      - todo-service
      - frontend
    volumes:
      - ./loadTests:/opt/loadTest
    environment:
      - TODOSERVICE_HOST=todo-service
      - TODOSERVICE_PORT=8443
EOF

sed s"/\$flavor/$dockerfile_flavor/g" docker-compose.tpl | tee docker-compose.yml
rm docker-compose.tpl

echo
echo
echo "To build and run execute"
echo "    $ docker-compose up --build"

