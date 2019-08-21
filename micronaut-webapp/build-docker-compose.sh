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
  frontend:
    build: 
      context: ./frontend
      dockerfile: Dockerfile$flavor
    ports:
      - 8081:8081
    environment:
      - TODOSERVICE_URL=http://todo-service:8080
  todo-service:
    build:
      context: ./todo-service
      dockerfile: Dockerfile$flavor
    ports:
      - 8080:8080
EOF

sed s"/\$flavor/$dockerfile_flavor/g" docker-compose.tpl | tee docker-compose.yml
rm docker-compose.tpl

echo
echo
echo "To build and run execute"
echo "    $ docker-compose up --build"

