GraalVM on Azure Container Instances Demo
=============================
This demo will walk you through the process of containerizing a Native Image application and then launching the image on the Azure platform by using the Azure Container Registry and Azure Container Instances. In this demo, you will deploy a simple "Hello World" HTTP application and have the ability to see details about its performance.

Prerequisites
----------------------
Ensure that you have the following installed and follow the linked instructions for any that you are missing:
- Docker: https://docs.docker.com/desktop/
- Azure CLI: https://learn.microsoft.com/en-us/cli/azure/install-azure-cli
- GraalVM: https://www.graalvm.org/downloads/

**COMPATIBILITY**: Please note that this demo must be performed on an x86-based system in order to properly function. Working through this demo on an ARM-based system will result in the generation of a native image executable that is not compatible with the platform.

Download or clone GraalVM demos repository:
```sh
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
```sh
cd graalvm-demos/native-azure-container-instances
```
2. Create a new Azure resource group that will store all resources for this demo:
```sh
az group create --name nativeResourceGroup --location <REGION>
```
<img width="766" alt="Create resource group" src="https://github.com/egadbois/graalvm-demos/assets/134104678/42cc4730-5909-4cde-8bed-42e4f13cde73">


**NOTE**: Available regions will be dependent on your current subscription, use the following command to see a list of those available to you:
```sh
az account list-locations
```

3. Create a container registry within the resource group (Chosen name must be unique across Azure and contain 5-50 lowercase alphanumeric characters):
```sh
az acr create --resource-group nativeResourceGroup --name <REGISTRY-NAME> --sku Basic
```
<img width="1185" alt="Create container instance" src="https://github.com/egadbois/graalvm-demos/assets/134104678/a7c6fc1a-f746-41f3-a774-c16688e0a3cf">


A successful creation will output something similar to that shown in the screenshot above. Take note of the "loginServer" value as this is the fully qualified registry name.

4. On an internet browser, open the [Azure dashboard](https://portal.azure.com/#home)
5. Use the search bar at the top of the page to navigate to the "Container registries" page
6. Select the registry you previously created and select "Access keys" on the left side of the page
7. Toggle the "Admin User" switch so that it is enabled - a username and password(s) will appear that you will use to login to the registry on the CLI

![Screen Shot 2023-06-20 at 12 50 39 PM](https://github.com/egadbois/graalvm-demos/assets/134104678/c732cb96-14d1-428c-a23a-0637b22ee8ee)


8. To provide your credentials to Docker, run the following command and use the Username and Password from the previous step when prompted:
```sh
docker login <REGISTRY-NAME>.azurecr.io
```
<img width="613" alt="Login success" src="https://github.com/egadbois/graalvm-demos/assets/134104678/eede8bfc-a461-4b87-8ad8-1f31612929ea">


__OPTIONAL__: In the next step you will use a single command to build the application into a container image and deploy it to the repository you have created; if you would like to first view the Docker file that will be used to create the image, run the following command:
```sh
./mvnw mn:dockerfile -Dpackaging=docker-native
```
The newly created Dockerfile will be automatically stored in the "target" directory

9. Use the Uri once again to push the image to the ACR:
```sh
./mvnw deploy -Dpackaging=docker-native -Djib.to.image=<REGISTRY-NAME>.azurecr.io/nativedemo
```
<img width="613" alt="Screen Shot 2023-06-20 at 1 09 02 PM" src="https://github.com/egadbois/graalvm-demos/assets/134104678/94d1ae3b-a8ea-485c-98c7-4070e92a9aa8">

Deploy the application on Azure Container Instances
----------------------
1. Create a container within the same resource group that you created in the last section (When prompted, enter the registry username and password as you did in the previous section):
```sh
az container create --resource-group nativeResourceGroup --name nativecontainer --image <REGISTRY-NAME>.azurecr.io/nativedemo --dns-name-label nativeapp --ports 8080
```
<img width="1192" alt="Create container" src="https://github.com/egadbois/graalvm-demos/assets/134104678/f0ee6551-e6e0-42b8-9ca8-c9702b230331">


2. Ensure that the provision has been successful:
```sh
az container show --resource-group nativeResourceGroup --name nativecontainer --query "{FQDN:ipAddress.fqdn,ProvisioningState:provisioningState}" --out table
```
<img width="1289" alt="Successful provision" src="https://github.com/egadbois/graalvm-demos/assets/134104678/d14c5c81-2959-4eec-bccc-303f440c0630">


3. If the ProvisioningState is "Succeeded" then you have properly deployed the application; to test it out use the outputted FQDN in your internet browser in the format:
```sh
<FQDN>:8080/hello
```

You should see a "Hello World" message displayed on the webpage:

![Screen Shot 2023-06-20 at 1 31 20 PM](https://github.com/egadbois/graalvm-demos/assets/134104678/d902babc-8516-4b3d-a177-51261c6820fb)

Clean-up
---------------------
Once you are completed with this demo, follow these steps to clean-up the resources created and ensure that you do not incur any charges:
1. On an internet browser, open the [Azure dashboard](https://portal.azure.com/#home)
2. Use the search bar at the top of the screen to navigate to the "Resource groups" page
3. Select the resource group that you created, click "Delete resource group", and then follow the on-screen instructions to confirm the deletion

![Screen Shot 2023-06-23 at 3 43 31 PM](https://github.com/egadbois/graalvm-demos/assets/134104678/a53da96b-5ba1-46ea-a071-7b3dd756080f)

