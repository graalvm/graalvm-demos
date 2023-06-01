GraalVM on AWS Fargate Demo
=============================
This demo will walk you through the process of containerizing a Native Image application and then launching the image on the AWS platform by using the Amazon Elastic Container Registry and AWS Fargate. In this demo, you will deploy a simple "Hello World" HTTP application and have the ability to see details about its performance.

Prerequisites
----------------------
Ensure that you have the following installed and follow the linked instructions for any that you are missing:
- Docker: https://docs.docker.com/desktop/
- Apache Maven: https://maven.apache.org/install.html
- Amazon Web Service CLI: https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html
    - Once installed, configure your AWS credentials: https://docs.aws.amazon.com/cli/latest/userguide/cli-authentication-user.html#cli-authentication-user-configure-wizard
- Amazon Elastic Container Service: https://docs.aws.amazon.com/AmazonECR/latest/userguide/get-set-up-for-amazon-ecr.html

1. Download and install the latest GraalVM JDK with Native Image using the [GraalVM JDK Downloader](https://github.com/graalvm/graalvm-jdk-downloader).
    ```bash
    bash <(curl -sL https://get.graalvm.org/jdk) 
    ```
2. Download or clone GraalVM demos repository:
    ```bash
    git clone https://github.com/graalvm/graalvm-demos
    ```
    
Deploy a Native Image Container on Amazon ECR
----------------------
1. Navigate to the directory for this demo:
```bash
cd graalvm-demos/aws-fargate
```
2. Build the container image:
```
mvn -Pnative spring-boot:build-image
```
3. Create a new ECR repository to store the image:
```
aws ecr create-repository --repository-name native-fargate-repo
```
4. Use the outputted *repositoryUri* to tag the image:
```
docker tag native-fargate-demo:0.0.1-SNAPSHOT REPOSITORYURI
```
5. Authenticate the Uri for the repository with your credentials:
```
docker login -u AWS -p $(aws ecr get-login-password) REPOSITORYURI
```
6. Use the Uri once again to push the image to the Amazon ECR:
```
docker push REPOSITORYURI
```

Deploy the Service on AWS Fargate
-------------------------
1. Login to the [Amazon Web Service dashboard](http://console.aws.amazon.com/)
2. In the “Services” drop-down menu on the top-left of the page, select “All services” and navigate to the “Elastic Container Service” page
3. Click on "Clusters" on th left-side pane
4. Create a new cluster using the button on the top-right of the page
5. Choose a name for the cluster and leave the remaining settings as their default
    - If you would like to be able to view insights about the container image perforamance, activate Container Insights under "Monitoring"
6. Once you have created the new cluster, navigate to "Task definitions" by selecting it on the left-side pane
7. Create a new task definition by clicking the button on the top-right corner
8. Choose names for the Task Definition Family and Container
9. Paste the REPOSITORYURI used in the prior section into the "Image URI" text box
    - To find the URI again click the "Amazon ECR" link on the left-side pane and select "Repositories". Your repository will appear along with the corresponding URI
10. Leave the remaining options as their default and create the new Task Definition
11. Return to the list of Clusters and select the Cluster that you created
12. Under "Services" click the "Create" button
13. Choose "Task" as the Application Type
14. Choose the newly created Task Definition under the "Family" drop-down menu and choose a name for the service
15. Under "Networking" -> "Security Group" select "Create a new security group"
16. Choose a name and description for the new group
17. For "Type" select "All traffic" and for "Source" select "Anywhere"
18. Click "Create" to create and deploy the service
19. Select the new service that you created and navigate to the "Tasks" tab
20. Select the task currently running and copy the public IP address displayed on the right side of the page
21. In a new browser tab, type the IP address in the format: http://PUBLICIP:80/hello
22. You should see a "Hello World" message displayed!

Clean-Up
-----------------
To prevent incurring additional charges after you are finished with the demo you first need to stop the service that you created. To do so, follow these instructions:
1. Visit the Task Definitions page and select the task currently running
2. Under Actions select "Deregister"
3. Change the drop-down view menu to show "Inactive task definitions"
4. Select the task definition and under Actions select "Delete"
5. Return to the cluster that you created and delete the service currently running
6. Use the button on the top-right of the screen to delete the cluster itself
