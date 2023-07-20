GraalVM on Google Cloud Run Demo
================================
This demo will walk you through the processes for deploying Native Image applications onto the Google Cloud Run platform. In this demo, you will deploy a simple "Hello World" HTTP application and have the ability to see details about its performance.

Prerequisites
----------------------
Ensure that you have the following installed and follow the linked instructions for any that you are missing:
- Docker: https://docs.docker.com/desktop/
- Google Cloud CLI: https://cloud.google.com/sdk/docs/install#linux
- Apache Maven: https://maven.apache.org/install.html
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

Deploying a Native Image Application
----------------------
1. Navigate to the directory for this demo:
```sh
cd graalvm-demos/native-google-cloud-run
```
2. Login to your Google account using the Google Cloud CLI:
```sh
gcloud auth login
```
3. Run the following command to configure Docker credentuals for the CLI:
```sh
gcloud auth configure-docker
```
4. Create a new project using the following command, where "xxxxxx" denotes the 6-digit identifier you choose for your project:
```sh
gcloud projects create graal-demo-xxxxxx
```
5. Set your newly created project to be currently selected:
```sh
gcloud config set project graal-demo-xxxxxx
```
To see a list of all projects use:
```sh
gcloud projects list
```
6. Use a browser to login to the Google Cloud dashboard and navigate to the [Billing](https://console.cloud.google.com/billing/projects) tab
7. Ensure that you have a billing account set up and enable billing for the newly created project by clicking on the dots beside the project name and selecting "Change billing"
<img width="1478" alt="Billing" src="https://github.com/egadbois/graalvm-demos/assets/134104678/76fb6173-967e-442f-9e0b-8ef23b806165">


8. Back on your terminal interface, activate the required project APIs:
```sh
gcloud services enable run.googleapis.com container.googleapis.com
```
__OPTIONAL__: In the next step you will use a single command to build the application into a container image and deploy it to the repository you have created; if you would like to first view the Docker file that will be used to create the image, run the following command:
```sh
./mvnw mn:dockerfile -Dpackaging=docker-native
```
The newly created Dockerfile will be automatically stored in the "target" directory

9. Push the application image to Google Cloud Container Registry (once again replacing "xxxxxx" as appropriate):
```sh
./mvnw deploy -Dpackaging=docker-native -Djib.to.image=gcr.io/graal-demo-xxxxxx/graaldemo:latest
```
<img width="891" alt="Build and deploy" src="https://github.com/egadbois/graalvm-demos/assets/134104678/f5911964-77af-4562-ac6a-ee22ace4db5f">

10. Deploy the application to Google Cloud Run:
```sh
gcloud run deploy --image=gcr.io/graal-demo-xxxxxx/graaldemo:latest --platform managed --allow-unauthenticated
```
<img width="1010" alt="Deploy on Run" src="https://github.com/egadbois/graalvm-demos/assets/134104678/2deab8a6-26da-4e5b-b48b-2bf6064d222a">

11. Once the application is successfully deployed, a Service URL will be outputted. Use that URL in the following command to test the application - a success will return the string "Hello World":
```sh
curl SERVICE_URL/hello
```
<img width="634" alt="Hello World" src="https://github.com/egadbois/graalvm-demos/assets/134104678/9fdc8d98-777c-4496-ba14-dcd330297480">

12. To view detailed information about the application performace such as build & response times, visit the [Google Cloud Logging](https://console.cloud.google.com/logs/) page

Clean-Up
-----------------------
Once you are finished with the demo, follow these steps to delete the created resources:
1. Use a browser to login to the Google Cloud dashboard and navigate to the [Resource Manager](https://console.cloud.google.com/cloud-resource-manager?previousPage=%2Fprojectselector2&organizationId=0)
2. Check the box next to the name of the project you wish to delete

![Screen Shot 2023-06-26 at 3 27 51 PM](https://github.com/egadbois/graalvm-demos/assets/134104678/c0521d4f-136a-49cf-99b0-dc3f35094225)

3. Click the "DELETE" button on the top bar and follow the on-screen instructions