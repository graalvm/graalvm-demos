# GraalVM Java to JavaScript Polyglot debugging
This sample code helps to demonstrate simplicity of debugging and developing GraalVM Polyglot apps using __VSCode extensions__.

## Prerequisities
1. VSCode has to be installed
2. Then go to VSCode Extensions activity panel and search for _GraalVM Extension Pack_, install it. The GraalVM Extension Pack will install all required extensions.
3. Navigate to __Gr__ activity panel in VSCode and install some of the latest GraalVMs from the list.
4. Confirm to set this as _Active GraalVM_ for VSCode.

## Debugging the project
Working with Polyglot project in VSCode is simple just open this sample. It will be working out of the box with VSCode GraalVM extensions.

Place breakpoints either in Java or JavaScript code.

To debug the project go to __Run and Debug__ activity or simply press F5. Choose ___Launch Java 8+___ as this is the Polyglot debugger provided by GraalVM extensions.

That is, the sample is running under debugger on GraalVM. 