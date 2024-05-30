# OCI Native Generative AI Example

This example illustrates how to use the Generative AI service provided by Oracle Cloud Infrastructure (OCI).
It uses a state-of-the-art, customizable large language model to generate text. 

The [source code](./src/main/java/com/oracle/labs/GenerateTextExample.java) is a self-contained demonstration of using a [`GenerativeAiInferenceClient`](https://docs.oracle.com/iaas/tools/java/latest/com/oracle/bmc/generativeaiinference/GenerativeAiInferenceClient.html) to generate text, based on a prompt.

The [_pom.xml_](pom.xml) file includes necessary dependents (such as the [OCI Java SDK](https://docs.public.oneportal.content.oci.oraclecloud.com/iaas/Content/API/SDKDocs/javasdk.htm)) and configures the build, including the [Native Maven Plugin](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html) to generate a native executable with GraalVM Native Image.

> For more information, see [Generative AI](https://docs.oracle.com/iaas/Content/generative-ai/home.htm).

## Prerequisites
* [GraalVM](https://www.graalvm.org/downloads/).
* You must have access to access to Generative AI resources in your tenancy/compartment. 
(For more information, see [Getting Access to Generative AI](https://docs.oracle.com/iaas/Content/generative-ai/iam-policies.htm).)

## Preparing Your Code

Download or clone the GraalVM demos repository:
```sh
git clone https://github.com/graalvm/graalvm-demos
```

Edit the contents of [_GenerateTextExample.java_](./src/main/java/com/oracle/labs/GenerateTextExample.java) as follows:

1. Update the value of the static variable `CONFIG_PROFILE` with the name of your OCI profile name.

2. Update the value of the static variable `COMPARTMENT_ID` with the OCID of a compartment that has access to Generative AI resources.
(For more information, see [Finding the OCID of a Compartment](https://docs.oracle.com/iaas/Content/GSG/Tasks/contactingsupport_topic-Locating_Oracle_Cloud_Infrastructure_IDs.htm#Finding_the_OCID_of_a_Compartment).)

Alternatively, you can follow the steps in [Generate Text in Generative AI](https://docs.oracle.com/iaas/Content/generative-ai/use-playground-generate.htm):

* (Optional) Enter your own prompt&mdash;you can edit this in the source code later.
* (Optional) Set values for the parameters&mdash;you can adjust these in the source code later.
* When you are happy with your prompt and the output, click **View code**, select **Java** as the programming language, then click **Copy code**, paste the code into the file named _GenerateTextExample.java_ and save it.
    > Your code should resemble [_GenerateTextExample.java_](./src/main/java/com/oracle/labs/GenerateTextExample.java).

## Running Your Code

Run the following command:

```shell
mvn install exec:java
```

This command runs the `main()` method in _GenerateTextExample.java_ and generates a response based on the prompt and parameters of the request.
The response should resemble the following:
```
GenerateTextResult(super=BmcModel(__explicitlySet__=[modelId, modelVersion, inferenceResponse])modelId=ocid1.generativeaimodel.oc1.us-chicago-1.amaaaaaask7dceyafhwal37hxwylnpbcncidimbwteff4xha77n5xz4m7p6a, modelVersion=15.6, inferenceResponse=CohereLlmInferenceResponse(super=LlmInferenceResponse(super=BmcModel(__explicitlySet__=[generatedTexts, timeCreated])), generatedTexts=[GeneratedText(super=BmcModel(__explicitlySet__=[id, text])id=0dbcc447-2563-41e3-b742-fd1d1b465073, text= We're looking for a talented Data Visualization Expert to join our team! The ideal candidate will have at least 5 years of experience in creating compelling and insightful data visualizations, as well as a keen eye for detail and the ability to think creatively.

As a Data Visualization Expert, you will be responsible for designing and developing visually appealing and effective representations of data and information. You will work closely with our team of data analysts and scientists to understand the data and the story it needs to tell, and then create customized visualizations that help convey that story to a wide audience in a clear and concise manner.

Your responsibilities will include:

Collaborating with data analysts and scientists to understand data sets and determine the best ways to visualize the information they contain.
Creating original and innovative data visualizations that effectively communicate the key insights and findings to both technical and non-technical stakeholders.
Paying close attention to details such as design, color theory, and typography to create visually appealing and accurate representations of data.
Proactively researching and staying up-to-date on industry trends and best practices in data visualization.
Conducting experiments with different visualization techniques and tools to determine the most effective ways to convey complex data sets.

Qualifications

At least 5 years of experience in data visualization or a related field.
A portfolio of work showcasing your data visualization projects and the impact they had on decision-making.
Excellent communication skills, both verbal and written, with the ability to explain complex data concepts to both technical and non-technical stakeholders.
Strong analytical skills with the ability to interpret and understand large data sets.
Great attention to detail, with the ability to spot trends and patterns within data.
Proficiency in data visualization tools such as Tableau, Power BI, or D3.js, or the ability to learn and master new tools quickly.
An understanding of design principles and typography is a plus.

We are looking for someone who is passionate about data and storytelling, and who has the ability to turn data into actionable insights through innovative visualization. If you have the relevant experience and skills, we want to hear from you! 

This is a great opportunity to join a dynamic and growing team and make a meaningful impact on our company's data-driven decisions. 

We look forward to receiving your application! , likelihood=null, finishReason=null, tokenLikelihoods=null)], timeCreated=Thu May 09 16:51:49 BST 2024, prompt=null))
```

## Running the Example as Native Executable

1. Create a native executable by running the following command:
    ```shell
    mvn clean package -Pnative
    ```

2. Run the native executable as shown below. (The response should resemble the one above.):
    ```shell
    ./target/generative-ai
    ```