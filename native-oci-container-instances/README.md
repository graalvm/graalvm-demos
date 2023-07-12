GraalVM on OCI Container Instances Demo
=============================
This demo will walk you through the process of containerizing a Native Image application and then launching the image on the OCI platform by using the Container Instance service. In this demo, you will deploy a simple "Hello World" HTTP application and have the ability to see details about its performance.

Prerequisites
----------------------
Ensure that you have the following installed and follow the linked instructions for any that you are missing:
- Docker: https://docs.docker.com/desktop/
- GraalVM: https://www.graalvm.org/downloads/
- Your OCI account also must have the proper permissions to create container instances; follow this guide to grant access: https://docs.oracle.com/en-us/iaas/Content/container-instances/permissions/policy-reference.htm#examples__let-users-create-container-instances

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

Configure authentication
----------------------
1. Open the OCI [dashboard](https://cloud.oracle.com/) and login to your profile
2. Open the Profile menu on the top-right of the screen and click "User Settings"
3. Under "Auth Tokens", click "Generate Token"
4. After choosing a description, the token will be presented to you. Immediately copy the token to a secure location because you will not be able to view it again in the console
5. On your local machine's terminal (where Docker is installed), login to the Registry:
```sh
docker login <region-key>.ocir.io
```
For a list of all region keys, [click here](https://docs.oracle.com/en-us/iaas/Content/Registry/Concepts/registryprerequisites.htm#regional-availability)

6. When prompted for a username, provide yours in the format:
```sh
<tenancy-namespace>/<username>
```
Tenancy namespace can be found on the [Tenancy Information](https://cloud.oracle.com/tenancy) page if you do not know it

7. Use the authorization token to created previously as the password when prompted

Deploy a Native Image Container on OCI Container Registry
----------------------
1. Navigate to the directory for this demo:
```sh
cd graalvm-demos/native-oci-container-instances
```

__OPTIONAL__: In the next step you will use a single command to build the application into a container image and deploy it to the repository you have created; if you would like to first view the Docker file that will be used to create the image, run the following command:
```sh
./mvnw mn:dockerfile -Dpackaging=docker-native
```
The newly created Dockerfile will be automatically stored in the "target" directory

2. Build the container image and push it to the OCI registry (```<repo-name>``` may be an existing repository or any name for a repository that will be created upon pushing the image):
```sh
./mvnw deploy -Dpackaging=docker-native -Djib.to.image=<region-key>.ocir.io/<tenancy-namespace>/<repo-name>
```
3. On a browser, visit the OCI dashboard and open the side menu to locate "Developer Services" -> "Containers & Artifacts" -> "Container Registry"
4. Select the directory in which you stored your image (the location corresponds to the ```<tenancy-namespace>``` that you tagged the image with)
5. Ensure that the Access type is "Public"; if it is not, click "Actions" in the top-right corner and select "Change to public"
<img width="888" alt="public repo" src="https://github.com/graalvm/graalvm-demos/assets/134104678/c57e9470-e455-401d-857d-40eb8c042591">


Create an OCI Container Instance
-------------------------
1. From the main OCI dashboard, open the side menu and click on "Developer Services" -> "Containers & Artifacts" -> "Container Instances"
<img width="450" alt="Screen Shot 2023-06-06 at 4 28 20 PM" src="https://github.com/egadbois/graalvm-demos/assets/134104678/5aea839b-ab79-4eec-a0b5-74927ffde179">

2. Click "Create container instance"
3. Input a name and compartment location for the instance
4. In the "Shape" section, adjust the sliders to your desired amounts of OCPUs and Memory (For this demo, 4 OCPUs and 128 GBs will be plenty)
<img width="1002" alt="Screen Shot 2023-06-06 at 4 29 11 PM" src="https://github.com/egadbois/graalvm-demos/assets/134104678/d71fd5a4-e6db-401e-b205-8e814b1e9091">

5. In the "Networking" section, you may either use a pre-existing VCN or create a new one
6. Leave the remaining options as their default and click "Next"
7. Choose a name for the container (or leave as default) and in the "Image" section click "Select image"
<img width="987" alt="Create container" src="https://github.com/graalvm/graalvm-demos/assets/134104678/092d4a12-426d-4617-b8a7-3e069d9c1550">


8. Locate the image that you pushed in the previous section according to the ```<tenancy-namespace>``` directory that you chose
9. Create the container and allow it a moment to intialize
<img width="1282" alt="Create container" src="https://github.com/graalvm/graalvm-demos/assets/134104678/dba8c624-1fff-4c2c-883e-0287b910ff95">


10. A successful deployment will result in the large icon box turning green and the status "ACTIVE" displayed beneath it
<img width="1493" alt="Successful deployment" src="https://github.com/graalvm/graalvm-demos/assets/134104678/69c46d53-1e42-451d-a2fd-b1a2b812ec35">


Configure the Security Group
------------------------
1. On the OCI platform, open the side menu and click on "Networking" -> "Virtual cloud networks"
2. Click on the VCN that your container instance is using (If you do not see the VCN, ensure that you are in the correct compartment via the drop-down menu on the left side of the screen)
3. Click "Network Security Groups" and then "Create Network Security Group"
4. Choose a name and compartment for the new group then click "Next"
5. Select "Ingress" as the Direction, CIDR for Source Type, "0.0.0.0/0" for Source CIDR, and leave the remaining fields as their default - then click "Create"
<img width="1493" alt="Screen Shot 2023-06-07 at 12 28 43 PM" src="https://github.com/egadbois/graalvm-demos/assets/134104678/4099ec70-1b73-43ab-8f83-5813dbc71dac">

6. Return to the page for your container instance and click "Edit" beside "Network security groups"
7. Locate the newly created security group and click "Save changes"
8. To test the deployment, copy the Public IP address and input it into your web browser in the format:
```sh
http://<public-ip-address>:8080/hello
```
9. If you have completed the demo successfully, a "Hello World" message will be displayed!

![Hello World](https://github.com/egadbois/graalvm-demos/assets/134104678/593016fb-6623-4757-aeab-723d93c010ad)

Clean-Up
---------------------------
Once you have completed this demo, follow these instructions to delete the created resources:
1. Visit the OCI [dashboard](https://cloud.oracle.com/)
2. Open the side menu and click on "Developer Services" -> "Containers & Artifacts" -> "Container Registry"
3. Select the container image that you uploaded and under the "Actions" drop down select "Delete repository"

![Delete registry](https://github.com/graalvm/graalvm-demos/assets/134104678/b8135869-fd64-48f9-989a-83c188e8b5eb)

4. On the left side of the page, select "Container Instances"
5. Select the container instance that you created and under the "More actions" drop down select "Delete"

![Delete instance](https://github.com/graalvm/graalvm-demos/assets/134104678/cb439c80-6155-47de-956f-031469741e1f)

6. Open the side menu and click on "Networking" -> "Virtual cloud networks"
7. Select the VCN that you created for your container instance
8. Click the red "Delete" button

![Delete VCN](https://github.com/graalvm/graalvm-demos/assets/134104678/34ed6396-73f1-4f2d-9ec7-409b463b295a)

9. Click "Scan" - this will return a list of any lingering resources that are referencing the VCN
10. Click "Delete all" to remove the listed resources (if this fails you can select each resource individually to delete them on their own pages)
