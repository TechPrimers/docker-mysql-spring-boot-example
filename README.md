# Playing with Springboot and MySQL on Kubernetes
In this project, we’ll learn how to build and deploy a Stateful app built with Java Spring Boot, Mysql on Kubernetes
This solution allows you to automate the deploymen and management of the application containers.
The first step is to obtain the application source code and Dockerfile.
and use them as a starting point for creating a custom yaml manifests to automate the application deployment in a Kubernetes cluster.

> I used Minikube to deploy this Springboot application, but it should work with any cloud provider or distribution.

## Assumption And Prerequisites

- You have a Docker environment running.
- You have an account in a container registry (this project assumes that you are using Docker Hub).
- You have Minikube installed in your local computer.
- You have the kubectl command line (kubectl CLI) installed.
- You have Java JDK v8 installed
- You have Maven installed on your machine for local deployment
- You have Helm v3 installed on your machine for deployment (optional)

## Steps:

To build and deploy our application  on Kubernetes, we will typically follow these steps:

1.  Build the Docker image
2.  Publish the Docker image
3.  Deploy the application in Kubernetes

## Build the Application with Maven

To package the Spring Boot Application, you can just run: `$ mvn package`

## Build The Docker Image

The source code already contains the Dockerfile needed for the Springboot app.

1. The  `Dockerfile` for creating a docker image from the Spring Boot Application:

````
FROM openjdk:8
ADD target/users-mysql.jar users-mysql.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "users-mysql.jar"]
````


2. Build the image using the command below:

* `$ docker build -t users-mysql .`

## Publish The Docker Image

To upload the image to Docker Hub Repository (already exists!), follow the steps below:
 [In this examle, for Developing purpose, I used my personal DockerHub Registery]
* Log in to Docker Hub: `$ docker login`

* Tag and Push the image to your Docker Hub account. (you can replace the username placeholder with your Docker ID:

`$ docker tag users-mysql:latest  <docker_user>/users-mysql:version>`

`<docker_user> = aymensegni` &
`version = v0.0.1` in this example


`$ docker push <docker_user>/users-mysql:<version>`


## Create Application environment Secrets

````
|--⫸  kubectl create secret generic mysql-root-pass --from-literal=password=password
secret/mysql-root-pass created
|~/run-it-on-cloud/docker-mysql-spring-boot-example 💻 --⑆ dev 🔧
|
|--⫸  kubectl create secret generic mysql-user-pass --from-literal=username=sa --from-literal=password=password
secret/mysql-user-pass created
|~/run-it-on-cloud/docker-mysql-spring-boot-example 💻 --⑆ dev 🔧
|
|--⫸  kubectl create secret generic mysql-db-url --from-literal=database=test --from-literal=url='jdbc:mysql://mysql-standalone:3306/test?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false'
secret/mysql-db-url created
|~/run-it-on-cloud/docker-mysql-spring-boot-example 💻 --⑆ dev 🔧
|
|--⫸

````

## Verfiy secrets creation


````
|--⫸  kubectl get secrets
NAME                  TYPE                                  DATA   AGE
default-token-qw42v   kubernetes.io/service-account-token   3      6m11s
mysql-db-url          Opaque                                2      3m33s
mysql-root-pass       Opaque                                1      5m49s
mysql-user-pass       Opaque                                2      5m18s
````

## Deploy the `users-mysql` Application on Kubernetes

````
|--⫸  kubectl apply -f k8s-deployment/app-deployment.yaml
deployment.apps/users-mysql created
service/users-mysql created
````


## Verfiy Deployment

````
|--⫸  kubectl  get pods
NAME                                  READY   STATUS    RESTARTS   AGE
users-mysql-6f9858cfd-ptmjl           1/1     Running   0          94s
users-mysql-6f9858cfd-sngph           1/1     Running   0          94s

````

````
|--⫸  kubectl  get deployments.apps
NAME                 READY   UP-TO-DATE   AVAILABLE   AGE
users-mysql          2/2     2            2           3m16s

````

## Verfiy the apllication's service

````
|--⫸  minikube service list
|-------------|--------------------|----------------------------|-----|
|  NAMESPACE  |        NAME        |        TARGET PORT         | URL |
|-------------|--------------------|----------------------------|-----|
| default     | kubernetes         | No node port               |
| default     | users-mysql        | http://192.168.64.23:30793 |
| kube-system | kube-dns           | No node port               |

````

##  Deploy MySQL

````
|--⫸  kubectl apply -f k8s-deployment/mysql.yaml
persistentvolume/mysql-pv created
persistentvolumeclaim/mysql-pv-claim created
service/users-mysql-server created
deployment.apps/users-mysql-server created
````

## Verfiy MySQL Deployment : `user-mysql-server`

````
|--⫸  kubectl get deployments
NAME                 READY   UP-TO-DATE   AVAILABLE   AGE
users-mysql-server   1/1     1            1           61s
````


## Application service url

````
|--⫸  minikube service users-mysql --url
http://192.168.64.23:32173
````


