GraalVM on OCI Container Instances Demo
=============================
This demo will walk you through the process of containerizing a Native Image application and then launching the image on the OCI platform by using the Container Instance service. In this demo, you will deploy a simple "Hello World" HTTP application and have the ability to see details about its performance.

Prerequisites
----------------------
Ensure that you have the following installed and follow the linked instructions for any that you are missing:
- Docker: https://docs.docker.com/desktop/
- Apache Maven: https://maven.apache.org/install.html
- Your OCI account also must have the proper permissions to create container instances; follow this guide to grant access: https://docs.oracle.com/en-us/iaas/Content/container-instances/permissions/policy-reference.htm#examples__let-users-create-container-instances

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader).
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) 
    ```
2. Download or clone GraalVM demos repository:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```

Configure authentication
----------------------
1. Open the OCI [dashboard](https://cloud.oracle.com/) and login to your profile
2. Open the Profile menu on the top-right of the screen and click "User Settings"
3. Under "Auth Tokens", click "Generate Token"
4. After choosing a description, the token will be presented to you. Immediately copy the token to a secure location because you will not be able to view it again in the console
5. On your local machine's terminal (where Docker is installed), login to the Registry:
```
docker login <region-key>.ocir.io
```
For a list of all region keys, [click here](https://docs.oracle.com/en-us/iaas/Content/Registry/Concepts/registryprerequisites.htm#regional-availability)

6. When prompted for a username, provide yours in the format:
```
<tenancy-namespace>/<username>
```
Tenancy namespace can be found on the [Tenancy Information](https://cloud.oracle.com/tenancy) page if you do not know it

7. Use the authorization token to created previously as the password when prompted

Deploy a Native Image Container on OCI Container Registry
----------------------
1. Navigate to the directory for this demo:
```bash
cd graalvm-demos/oci-instances
```
2. Build the container image:
```
mvn -Pnative spring-boot:build-image
```
3. Tag the image with the location for it to be stored (```<repo-name>``` may be an existing repository or any name for a repository that will be created upon pushing the image):
```
docker tag oci-instance-demo:0.0.1-SNAPSHOT <region-key>.ocir.io/<tenancy-namespace>/<repo-name>
```
4. Push the image to the registry:
```
docker push <region-key>.ocir.io/<tenancy-namespace>/<repo-name>:latest
```
5. On a browser, visit the OCI dashboard and open the side menu to locate "Developer Services" -> "Containers & Artifacts" -> "Container Registry"
6. Select the directory in which you stored your image (the location corresponds to the ```<tenancy-namespace>``` that you tagged the image with)
7. Ensure that the Access type is "Public"; if it is not, click "Actions" in the top-right corner and select "Change to public"

Create an OCI Container Instance
-------------------------
1. From the main OCI dashboard, open the side menu and click on "Developer Services" -> "Containers & Artifacts" -> "Container Instances"
2. Click "Create container instance"
3. Input a name and compartment location for the instance
4. In the "Shape" section, adjust the sliders to your desired amounts of OCPUs and Memory (For this demo, 4 OCPUs and 128 GBs will be plenty)
5. In the "Networking" section, you may either use a pre-existing VCN or create a new one
6. Leave the remaining options as their default and click "Next"
7. Choose a name for the container (or leave as default) and in the "Image" section click "Select image"
8. Locate the image that you pushed in the previous section according to the ```<tenancy-namespace>``` directory that you chose
9. Create the container and allow it a moment to intialize; a successful deployment will result in the large icon box turning green and the status "ACTIVE" displayed beneath it

Configure the Security Group
------------------------
1. On the OCI platform, open the side menu and click on "Networking" -> "Virtual cloud networks"
2. Click on the VCN that your container instance is using (If you do not see the VCN, ensure that you are in the correct compartment via the drop-down menu on the left side of the screen)
3. Click "Network Security Groups" and then "Create Network Security Group"
4. Choose a name and compartment for the new group then click "Next"
5. Select "Ingress" as the Direction, CIDR for Source Type, "0.0.0.0/0" for Source CIDR, and leave the remaining fields as their default - then click "Create"
6. Return to the page for your container instance and click "Edit" beside "Network security groups"
7. Locate the newly created security group and click "Save changes"
8. To test the deployment, copy the Public IP address and input it into your web browser in the format:
```
http://<public-ip-address>:8080/hello
```
If you have completed the demo successfully, a "Hello World!" message will be displayed!