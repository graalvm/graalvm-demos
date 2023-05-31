GraalVM on AWS Lambda Demo
====================
This demo will walk you through the processes for deploying both Java 17 and Native Image applications onto the AWS Lambda platform. In this demo, you will deploy a simple "Hello World" HTTP application and have the ability to compare how the versions of the application compare based on speed and size.

Prerequisites
----------------------
Ensure that you have the following installed and follow the linked instructions for any that you are missing:
- Docker: https://docs.docker.com/desktop/
- Apache Maven: https://maven.apache.org/install.html

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader).
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) 
    ```
2. Download or clone GraalVM demos repository:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```

Deploying a Java 17 Application (.jar)
----------------------
1. Navigate to the directory for this demo:
```
cd graalvm-demos/aws-lambda-demo
```
3. Generate the .jar application file by executing the following command (the file will be created in aws-lambda-demo/target):
```
mvn package
```
3. Sign-in to [Amazon Web Services](https://ca-central-1.console.aws.amazon.com/lambda/home?region=ca-central-1#/discover) and navigate to the Lambda dashboard
![AWS dashboard](https://github.com/egadbois/graalvm-demos/assets/134104678/b57492af-1205-4ff9-b498-c5e610688292)
4.	Select the “Create function” button in the top-right corner
5.	Select “Author from scratch”, choose a name for your function, select “Java 17” as the runtime, and select “x86_64” as the architecture
![Create function](https://github.com/egadbois/graalvm-demos/assets/134104678/fbf949cd-fc99-4a2b-a649-541912d44aca)
6.	On the page for your newly created function, navigate to the section titled “Code source” and select the “Upload from .zip or.jar file” button
![Code upload](https://github.com/egadbois/graalvm-demos/assets/134104678/13827b9c-c09e-45b2-ac49-2c39b56d33ff)
7.	Click upload and navigate to where you stored the function.jar file that you downloaded then select “upload” and “save”
8.	Under “Runtime settings”, click “Edit” and input “io.micronaut.function.aws.proxy.MicronautLambdaHandler” as the Handler
![Runtime settings](https://github.com/egadbois/graalvm-demos/assets/134104678/fdb57a3b-fe59-406a-9664-3e83f6595f19)
9.	Now you can go ahead and test the function. To do so, navigate to the “Test” tab and select “Create new event”. Choose a name for your test and input the following as the contents of the “Event JSON” box:
```
{
  "path": "/",
  "httpMethod": "GET",
  "headers": {
    "Accept": "application/json"
  }
}
```
![Test code](https://github.com/egadbois/graalvm-demos/assets/134104678/a23629d4-c91b-4a19-9e36-a156f5992101)

10.	Click “Save” in the top-right corner to save your test and click “Test” to ensure that the application works correctly. You should receive an output that indicates a success and provides information about the performance of the application:
![Test result](https://github.com/egadbois/graalvm-demos/assets/134104678/4c73886a-a955-4631-be83-60308223ec75)


Deploying a Native Image Application
----------------------------------
1. Navigate to the directory for this demo:
```
cd graalvm-demos/aws-lambda-demo
```
2. AWS Lambda requires a bootstrap file that provides instructions for running applications with custom runtimes. Generate a zip file containing the Native Image executable and a corresponding bootstrap file with the following command (the file will be created in aws-lambda-demo/target):
```
mvn package -Dpackaging=docker-native
```
3. Sign-in to [Amazon Web Services](https://ca-central-1.console.aws.amazon.com/lambda/home?region=ca-central-1#/discover) and navigate to the Lambda dashboard
![AWS dashboard](https://github.com/egadbois/graalvm-demos/assets/134104678/b57492af-1205-4ff9-b498-c5e610688292)
4.	Select the “Create function” button in the top-right corner
5.	Select “Author from scratch”, choose a name for your function, select “Provide your own bootstrap on Amazon Linux 2” as the runtime, and choose “x86_64” as the architecture
![Create function](https://github.com/egadbois/graalvm-demos/assets/134104678/ad5c9018-423c-4f29-b299-06fa9b3ad0f6)
6.	On the page for your newly created function, navigate to the section titled “Code source” and select the “Upload from .zip or.jar file” button
![Code upload](https://github.com/egadbois/graalvm-demos/assets/134104678/1f50b29d-ba91-45a8-beeb-7960c4712ebd)
7.	Click upload and navigate to where you stored the function.zip file that you downloaded then select “upload” and “save”
8.	Under “Runtime settings”, click “Edit” and input “io.micronaut.function.aws.proxy.MicronautLambdaHandler” as the Handler
![Runtime settings](https://github.com/egadbois/graalvm-demos/assets/134104678/5487b1b3-0086-4b34-aea9-a4eecfa313a9)
9.	Now you can go ahead and test the function. To do so, navigate to the “Test” tab and select “Create new event”. Choose a name for your test and input the following as the contents of the “Event JSON” box:
```
{
  "path": "/",
  "httpMethod": "GET",
  "headers": {
    "Accept": "application/json"
  }
}
```
![Test code](https://github.com/egadbois/graalvm-demos/assets/134104678/a23629d4-c91b-4a19-9e36-a156f5992101)

10.	Click “Save” in the top-right corner to save your test and click “Test” to ensure that the application works correctly. You should receive an output that indicates a success and provides information about the performance of the application:
![Test result](https://github.com/egadbois/graalvm-demos/assets/134104678/cc11c441-4fc0-4873-bba4-a5a96f78f8cd)
