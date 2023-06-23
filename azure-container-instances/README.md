GraalVM on Azure Container Instances Demo
=============================
This demo will walk you through the process of containerizing a Native Image application and then launching the image on the Azure platform by using the Azure Container Registry and Azure Container Instances. In this demo, you will deploy a simple "Hello World" HTTP application and have the ability to see details about its performance.

Prerequisites
----------------------
Ensure that you have the following installed and follow the linked instructions for any that you are missing:
- Docker: https://docs.docker.com/desktop/
- Azure CLI: https://learn.microsoft.com/en-us/cli/azure/install-azure-cli
- GraalVM: https://www.graalvm.org/downloads/

Download or clone GraalVM demos repository:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    
Micronaut "Hello World" Application
----------------------
The code provided in this demo is a simple "Hello World" REST application created using the Micronaut framework. To understand what the code is doing, take a look at the _Application.java_ and _HelloController.java_ files:

**Application.java**

<img width="469" alt="Application.java" src="https://github.com/egadbois/graalvm-demos/assets/134104678/a330ab66-c3d0-43ac-91ce-4abf11685234">

This is the location of the main() function and entry point for the application.

**HelloController.java**

<img width="497" alt="HelloController.java" src="https://github.com/egadbois/graalvm-demos/assets/134104678/e48d3a98-99e0-44ca-8b6c-2abdd07fa5dd">

This code implements the actual RESTful "Hello World" functionality. It produces the "Hello World" string when a GET request is made to the "/hello" URL.

Deploy a Native Image Container on Azure Container Registry
----------------------
1. Navigate to the directory for this demo:
```bash
cd graalvm-demos/azure-container-instances
```
2. Create a new Azure resource group that will store all resources for this demo:
```
az group create --name nativeResourceGroup --location <REGION>
```

**NOTE**: Available regions will be dependent on your current subscription, use the following command to see a list of those available to you:
```
az account list-locations
```

3. Create a container registry within the resource group (Chosen name must be unique across Azure and contain 5-50 lowercase alphanumeric characters):
```
az acr create --resource-group nativeResourceGroup --name <REGISTRY-NAME> --sku Basic
```

A successful creation will output something similar to that shown in the screenshot above. Take note of the "loginServer" value as this is the fully qualified registry name.

4. On an internet browser, open the [Azure dashboard](https://portal.azure.com/#home)
5. Use the search bar at the top of the page to navigate to the "Container registries" page
6. Select the registry you previously created and select "Access keys" on the left side of the page
7. Toggle the "Admin User" switch so that it is enabled - a username and password(s) will appear that you will use to login to the registry on the CLI
8. To provide your credentials to Docker, run the following command and use the Username and Password from the previous step when prompted:
```
docker login <REGISTRY-NAME>.azurecr.io
``
A successful authentication will return a "Login Succeeded" message

__OPTIONAL__: In the next step you will use a single command to build the application into a container image and deploy it to the repository you have created; if you would like to first view the Docker file that will be used to create the image, run the following command:
```
./mvnw mn:dockerfile -Dpackaging=docker-native
```
The newly created Dockerfile will be automatically stored in the "target" directory

5. Use the Uri once again to push the image to the ACR:
```
./mvnw deploy -Dpackaging=docker-native -Djib.to.image=<REGISTRY-NAME>.azurecr.io/nativedemo
```
