#!/bin/sh

dockerfile_flavor=
docker_registry=
if [[ $2 != "" ]]
then
    docker_registry=$2\\/
fi

tag=:native
case "$1" in
    graalvm* )
    dockerfile_flavor=-graalvm
    tag=:graalvm-ce
    ;;
    hotspot*)
    dockerfile_flavor=-hotspot
    tag=:openjdk8
    ;;
esac

TODO_SERVICE_IMAGE=${docker_registry}"micronaut-webapp_todo-service${tag}"
FRONTEND_IMAGE=${docker_registry}"micronaut-webapp_frontend${tag}"
LOADGEN_IMAGE=${docker_registry}"loadgeneration"

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
          imagePullPolicy: Always
          volumeMounts:
            - mountPath: "/tmp/results"
              name: persistent-vol
          ports:
          - containerPort: 8443
            protocol: TCP
        volumes:
          - name: persistent-vol
            persistentVolumeClaim:
              claimName: pers-pvc
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
  type: NodePort
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
          imagePullPolicy: Always
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
apiVersion: batch/v1
kind: Job
metadata:
  name: loadgeneration
spec:
  template:
    metadata:
      name: loadgeneration
    spec:
      containers:
      - name: loadgeneration
        image: $LOADGEN_IMAGE
        imagePullPolicy: Always
        volumeMounts:
          - mountPath: "/tmp/results"
            name: persistent-vol
        env:
          - name: TODOSERVICE_HOST
            value: "todo-service"
          - name: TODOSERVICE_PORT
            value: "8443"
          - name: LOADTESTS_RESULTS
            value: "/tmp/results"
      volumes:
        - name: persistent-vol
          persistentVolumeClaim:
            claimName: pers-pvc

      restartPolicy: Never
EOF


sed s"/\$LOADGEN_IMAGE/$LOADGEN_IMAGE/g" k8/todoloadtest_deployment.tpl > k8/todoloadtest_deployment.yml
rm k8/todoloadtest_deployment.tpl

cat > k8/storage_minikube.yml << "EOF"
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pers-vol
  labels:
    type: local
spec:
  capacity:
    storage: 1Gi
  accessModes:
    - ReadWriteOnce
  hostPath:
    path: "/data/pers-vol"
---
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: pers-pvc
spec:
  accessModes:
    - ReadWriteOnce
  storageClassName: ""
  volumeName: pers-vol
  resources:
    requests:
      storage: 1Gi
EOF


echo "Kubernetes deployment files create in k8/"
echo
echo "To deploy"
echo "    $ kubectl create -f k8"