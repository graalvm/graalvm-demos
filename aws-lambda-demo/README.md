GraalVM on Amazon Lambda Demo
====================

Deploying a Java 11 Application (.jar)
----------------------
1. Download *function.jar* from this repository
2. Sign-in to [Amazon Web Services](https://ca-central-1.console.aws.amazon.com/lambda/home?region=ca-central-1#/discover) and navigate to the Lambda dashboard
![AWS dashboard](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.16.14%20PM.png)
3.	Select the “Create function” button in the top-right corner
4.	Select “Author from scratch”, choose a name for your function, select “Java 11” as the runtime, and select “x86_64” as the architecture
![Create function](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.17.21%20PM.png)
5.	On the page for your newly created function, navigate to the section titled “Code source” and select the “Upload from .zip or.jar file” button
![Code upload](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.18.22%20PM.png)
6.	Click upload and navigate to where you stored the function.jar file that you downloaded then select “upload” and “save”
7.	Under “Runtime settings”, click “Edit” and input “io.micronaut.function.aws.proxy.MicronautLambdaHandler” as the Handler
![Runtime settings](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.19.32%20PM.png)
8.	Now you can go ahead and test the function. To do so, navigate to the “Test” tab and select “Create new event”. Choose a name for your test and input the following as the contents of the “Event JSON” box:
```
{
  "path": "/",
  "httpMethod": "GET",
  "headers": {
    "Accept": "application/json"
  }
}
```
![Test code](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.21.02%20PM.png)

9.	Click “Save” in the top-right corner to save your test and click “Test” to ensure that the application works correctly. You should receive an output similar to the following:
![Test result](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.15.18%20PM.png)

Deploying a Native Image Application
----------------------------------
1. Download *function.zip* from this repository
![AWS dashboard](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.16.14%20PM.png)
2. Sign-in to [Amazon Web Services](https://ca-central-1.console.aws.amazon.com/lambda/home?region=ca-central-1#/discover) and navigate to the Lambda dashboard
3.	Select the “Create function” button in the top-right corner
4.	Select “Author from scratch”, choose a name for your function, select “Provide your own bootstrap on Amazon Linux 2” as the runtime, and choose “x86_64” as the architecture
![Create function](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.47.17%20PM.png)
5.	On the page for your newly created function, navigate to the section titled “Code source” and select the “Upload from .zip or.jar file” button
![Code upload](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.18.22%20PM.png)
6.	Click upload and navigate to where you stored the function.zip file that you downloaded then select “upload” and “save”
7.	Under “Runtime settings”, click “Edit” and input “io.micronaut.function.aws.proxy.MicronautLambdaHandler” as the Handler
![Runtime settings](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.50.45%20PM.png)
8.	Now you can go ahead and test the function. To do so, navigate to the “Test” tab and select “Create new event”. Choose a name for your test and input the following as the contents of the “Event JSON” box:
```
{
  "path": "/",
  "httpMethod": "GET",
  "headers": {
    "Accept": "application/json"
  }
}
```
![Test code](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.21.02%20PM.png)

9.	Click “Save” in the top-right corner to save your test and click “Test” to ensure that the application works correctly. You should receive an output similar to the following:
![Test result](/lambda/Guide/Screen%20Shot%202023-05-11%20at%2012.52.27%20PM.png)
