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

TODO_SERVICE_IMAGE="todo-service${dockerfile_flavor}"
FRONTEND_IMAGE="frontend${dockerfile_flavor}"
LOADGEN_IMAGE="loadgeneration"

(
    if [[ $(docker images -q ${TODO_SERVICE_IMAGE}) == "" ]]
    then
        cd todo-service
        ./docker-build.sh -q "$1"
    fi
)

(
  if [[ $(docker images -q ${FRONTEND_IMAGE}) == "" ]]
  then
      cd frontend
      ./docker-build.sh -q "$1"
  fi
)

(
  if [[ $(docker images -q ${LOADGEN_IMAGE}) == "" ]]
  then
      cd loadTests
      ./docker-build.sh -q
  fi
)

if [ ! -d k8 ]
then
    mkdir k8
fi

cat > k8/todoservice_deployment.tpl << "EOF"
apiVersion: apps/v1
kind: Deployment
metadata:
  name: todo-service
  labels:
    app: todo-service
spec:
    selector:
      matchLabels:
        app: todo-service
    replicas: 1
    template:
      metadata:
        labels:
          app: todo-service
      spec:
        containers:
        - name: todo-service
          image: $TODO_SERVICE_IMAGE
          imagePullPolicy: IfNotPresent
          ports:
          - containerPort: 8443
            protocol: TCP
EOF
sed s"/\$TODO_SERVICE_IMAGE/$TODO_SERVICE_IMAGE/g" k8/todoservice_deployment.tpl > k8/todoservice_deployment.yml
rm k8/todoservice_deployment.tpl

cat > k8/todoservice_service.yml << "EOF"
apiVersion: v1
kind: Service
metadata:
  name: todo-service
  labels:
    app: todo-service
spec:
  selector:
    app: todo-service
  ports:
  - port: 8443
    targetPort: 8443
    protocol: TCP
  type: ClusterIP
EOF

cat > k8/todofrontend_deployment.tpl << "EOF"
apiVersion: apps/v1
kind: Deployment
metadata:
  name: todo-frontend
  labels:
    app: todo-frontend
spec:
    selector:
      matchLabels:
        app: todo-frontend
    replicas: 1
    template:
      metadata:
        labels:
          app: todo-frontend
      spec:
        containers:
        - name: todo-frontend
          image: $FRONTEND_IMAGE
          imagePullPolicy: IfNotPresent
          env:
            - name: TODOSERVICE_URL
              value: "https://todo-service:8443"
          ports:
          - containerPort: 8081
            protocol: TCP
EOF
sed s"/\$FRONTEND_IMAGE/$FRONTEND_IMAGE/g" k8/todofrontend_deployment.tpl > k8/todofrontend_deployment.yml
rm k8/todofrontend_deployment.tpl

cat > k8/todofrontend_service.yml << "EOF"
apiVersion: v1
kind: Service
metadata:
  name: todo-frontend
  labels:
    app: todo-frontend
spec:
  selector:
    app: todo-frontend
  ports:
  - port: 8081
    targetPort: 8081
    protocol: TCP
  type: NodePort
EOF

cat > k8/todoloadtest_deployment.tpl << "EOF"
apiVersion: apps/v1
kind: Deployment
metadata:
  name: todo-loadtest
  labels:
    app: todo-loadtest
spec:
    selector:
      matchLabels:
        app: todo-loadtest
    replicas: 1
    template:
      metadata:
        labels:
          app: todo-loadtest
      spec:
        containers:
        - name: todo-loadtest
          image: $LOADGEN_IMAGE
          imagePullPolicy: IfNotPresent
          volumeMounts:
          - mountPath: /opt/loadTest
            name: data
          env:
            - name: TODOSERVICE_HOST
              value: "todo-service"
            - name: TODOSERVICE_PORT
              value: "8443"
        volumes:
        - name: data
          emptyDir: {}
        initContainers:
        - name: init-todo-loadtest-1
          image: alpine
          volumeMounts:
          - mountPath: /opt/loadTest
            name: data
          command: ['sh', '-c', 'apk add git; git clone https://github.com/graalvm/graalvm-demos.git; cp graalvm-demos/micronaut-webapp/loadTests/* /opt/loadTest; rm -rf graalvm-demos']
        - name: init-todo-loadtest-2
          image: alpine
          command: ['sh', '-c', 'until echo "GET /" | nc  $TODO_SERVICE_SERVICE_HOST $TODO_SERVICE_SERVICE_PORT; do echo waiting for Todo service...; sleep 1; done;']
EOF
sed s"/\$LOADGEN_IMAGE/$LOADGEN_IMAGE/g" k8/todoloadtest_deployment.tpl > k8/todoloadtest_deployment.yml
rm k8/todoloadtest_deployment.tpl

echo
echo
echo "To deploy"
echo "    $ kubectl create -f k8"