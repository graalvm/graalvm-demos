GraalVM on Google Cloud Run Demo
================================
This demo will walk you through the processes for deploying Native Image applications onto the Google Cloud Run platform. In this demo, you will deploy a simple "Hello World" HTTP application and have the ability to see details about its performance.

Prerequisites
----------------------
Ensure that you have the following installed and follow the linked instructions for any that you are missing:
- Docker: https://docs.docker.com/desktop/
- Apache Maven: https://maven.apache.org/install.html
- Google Cloud CLI: https://cloud.google.com/sdk/docs/install#linux

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader).
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) 
    ```
2. Download or clone GraalVM demos repository:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```

Deploying a Native Image Application
----------------------
1. Navigate to the directory for this demo:
```bash
cd graalvm-demos/google-cloud-run
```
2. Login to your Google account using the Google Cloud CLI:
```
gcloud auth login
```
3. Run the following command to configure Docker credentuals for the CLI:
```
gcloud auth configure-docker
```
4. Create a new project using the following command, where "xxxxxx" denotes the 6-digit identifier you choose for your project:
```
gcloud projects create graal-demo-xxxxxx
```
5. Set your newly created project to be currently selected:
```
gcloud config set project graal-demo-xxxxxx
```
To see a list of all projects use:
```
gcloud projects list
```
6. Use a browser to login to the Google Cloud dashboard and navigate to the [Billing](https://console.cloud.google.com/billing/projects) tab
7. Ensure that you have a billing account set up and enable billing for the newly created project by clicking on the dots beside the project name and selecting "Change billing"
<img width="1478" alt="Screen Shot 2023-05-16 at 12 52 19 PM" src="https://github.com/egadbois/graalvm-demos/assets/134104678/f598f19e-6e64-4170-a1d8-f78733b9c44c">

8. Back on your terminal interface, activate the required project APIs:
```
gcloud services enable run.googleapis.com container.googleapis.com
```
9. Push the application image to Google Cloud Container Registry (once again replacing "xxxxxx" as appropriate):
```
mvn deploy -Dpackaging=docker-native -Djib.to.image=gcr.io/graal-demo-xxxxxx/graaldemo:latest
```
<img width="891" alt="Screen Shot 2023-05-16 at 12 47 20 PM" src="https://github.com/egadbois/graalvm-demos/assets/134104678/78047411-d8b1-46d2-b61c-24d50d6c817e">

10. Deploy the application to Google Cloud Run:
```
gcloud run deploy --image=gcr.io/graal-demo-xxxxxx/graaldemo:latest --platform managed --allow-unauthenicated
```
<img width="1010" alt="Screen Shot 2023-05-16 at 12 46 05 PM" src="https://github.com/egadbois/graalvm-demos/assets/134104678/b29ce6b1-304a-441c-8846-4c921f293a7a">

11. Once the application is successfully deployed, a Service URL will be outputted. Use that URL in the following command to test the application - a success will return the string "Hello World":
```
curl -i SERVICE_URL/hello
```
<img width="634" alt="Screen Shot 2023-05-16 at 12 45 40 PM" src="https://github.com/egadbois/graalvm-demos/assets/134104678/16bcc0cb-7c4d-4663-a088-3cc3428b5329">

12. To view detailed information about the application performace such as build & response times, visit the [Google Cloud Logging](https://console.cloud.google.com/logs/) page
