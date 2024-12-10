GraalVM on OCI Container Instances Demo
=============================
This demo will walk you through the process of containerizing a Native Image application and then launching the image on the OCI platform by using the Container Instance service. In this demo, you will deploy a simple "Hello World" HTTP application and have the ability to see details about its performance.

Prerequisites
----------------------
Ensure that you have the following installed and follow the linked instructions for any that you are missing:
- A Docker-API compatible container runtime such as [Rancher Desktop](https://docs.rancherdesktop.io/getting-started/installation/) or [Docker](https://www.docker.io/gettingstarted/)
  - Ensure that the daemon is actively running before beginning the demo
- Your OCI account also must have the proper permissions to create container instances; follow this guide to grant access: https://docs.oracle.com/en-us/iaas/Content/container-instances/permissions/policy-reference.htm#examples__let-users-create-container-instances

**COMPATIBILITY**: Please note that this demo must be performed on an x86-based platform in order to properly function. Working through this demo on an ARM-based platform will result in the generation of a native executable that is not compatible with the OCI platform.

Download or clone the GraalVM demos repository:
```sh
git clone https://github.com/graalvm/graalvm-demos
```

Micronaut "Hello World" Application
----------------------
The code provided in this demo is a simple "Hello World" REST application created using the Micronaut &reg; framework. To understand what the code is doing, take a look at the _Application.java_ and _HelloController.java_ files:

**Application.java**

```java
package example.micronaut;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
```

This is the location of the main() function and entry point for the application.

**HelloController.java**

```java
package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/hello") 
public class HelloController {
    @Get 
    @Produces(MediaType.TEXT_PLAIN) 
    public String index() {
        return "Hello World"; 
    }
}
```

This code implements the actual RESTful "Hello World" functionality. It produces the "Hello World" string when a GET request is made to the _/hello_ URL.

Configure authentication
----------------------
1. Open the OCI [console](https://cloud.oracle.com/) and login to your profile
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
Example:
```sh
./mvnw deploy -Dpackaging=docker-native -Djib.to.image=us-phoenix-1.ocir.io/developer/test-repo
```
3. In the Oracle Cloud Console, open the navigation menu, click **Developer Services**. Under **Containers & Artifacts**, click **Container Registry**.
4. Select the repository in which you stored your image (the location corresponds to the ```<tenancy-namespace>``` with which you tagged the image).
5. Ensure that the Access type is "Public"; if it is not, click **Actions** in the top-right corner and select "Change to public".

![Make public](img/actions%20public.png)

**OPTIONAL:** If you wish to keep the repository as "Private", complete the following steps (your account must have permissions to create dynamic groups and policies):
- If your image is stored in the root compartment, move it to the desired compartment by selecting "Move Compartment"
- In the navigation menu, click **Identity & Security**. Under **Identity**, click **Dynamic Groups**.
- Create a new dynamic group, choosing a name and inputting the following as a rule (replacing the OCID with that of the compartment where the image is stored):
```
ALL {resource.type='computecontainerinstance', resource.compartment.id = 'ocid1.compartment.oc1..aaaqba'}
```

![Dynamic Group](img/create%20dynamic%20group.png)

- In the navigation menu, click **Identity & Security**. Under **Identity**, click **Policies**.
- Create a new policy with a name of your choosing and ensure it is created in your compartment.
- Toggle the "Show manual editor" switch and input the following into the text box that appears (using the names of your dynamic group and compartment):
```
Allow dynamic-group <dynamic-group-name> to read repos in compartment <container-instance-compartment-name>
```

![Policy](img/create%20policy.png)

Create an OCI Container Instance
-------------------------
1. In the Oracle Cloud Console, open the navigation menu, click **Developer Services**. Under **Containers & Artifacts**, click **Container Instances**.

![Navigation](img/container%20instances.png)

2. Click **Create container instance**.
3. Enter a name and compartment location for the instance.
4. In the "Shape" section, adjust the sliders to your desired amounts of OCPUs and Memory (1 CPU and 1 GB of memory will be enough thanks to the power of native image!)

![Configure](img/shape.png)


5. In the "Networking" section, you may either use a pre-existing VCN or create a new one.
6. Leave the remaining options as their defaults and click **Next**.
7. Choose a name for the container (or leave as default) and in the "Image" section click **Select image**.

![Select Image](img/select%20image.png)

8. Locate the image that you pushed in the previous section according to the ```<tenancy-namespace>``` directory that you chose.
9. Create the container and allow it a moment to intialize.

![Create container](img/create%20container%20instance.png)


10. A successful deployment will result in the large icon box becoming green and the status "ACTIVE" displayed beneath it.

![Active deployment](img/active%20container%20instance.png)

Configure the Security Group
------------------------
1. On the OCI platform, open the side menu and click on "Networking" -> "Virtual cloud networks"
2. Click on the VCN that your container instance is using (If you do not see the VCN, ensure that you are in the correct compartment via the drop-down menu on the left side of the screen)
3. Click "Network Security Groups" and then "Create Network Security Group"
4. Choose a name and compartment for the new group then click "Next"
5. Select "Ingress" as the Direction, CIDR for Source Type, "0.0.0.0/0" for Source CIDR, and leave the remaining fields as their default - then click "Create"

![Create security group](img/create%20security%20group.png)

6. Return to the page for your container instance and click "Edit" beside "Network security groups"
7. Locate the newly created security group and click "Save changes"
8. To test the deployment, copy the Public IP address and input it into your web browser in the format:
```sh
http://<public-ip-address>:8080/hello
```
9. If you have completed the demo successfully, a "Hello World" message will be displayed!

![Hello World](img/hello%20world.png)

Clean-Up
---------------------------
Once you have completed this demo, follow these instructions to delete the created resources:
1. Visit the OCI [console](https://cloud.oracle.com/)
2. In the Oracle Cloud Console, open the navigation menu, click **Developer Services**. Under **Containers & Artifacts**, click **Container Registry**.
3. Select the container image that you uploaded and from the **Actions** drop-down list select **Delete repository**.

![Delete registry](img/delete%20repository.png)

4. On the left side of the page, click **Container Instances**.
5. Select the container instance that you created and from the **More actions** drop-down list select **Delete **.

![Delete instance](img/delete%20instance.png)

6. In the Oracle Cloud Console, open the navigation menu, click **Developer Services**and then click **Virtual cloud networks**.
7. Select the VCN that you created for your container instance.
8. Click **Delete**.

![Delete VCN](img/delete%20VCN.png)

9. Click **Scan** - this returns a list of any lingering resources that reference the VCN.
10. Click **Delete all** to remove the listed resources (if this fails you can select each resource individually to delete them on their own pages).
