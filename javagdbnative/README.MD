# Native Image Debugging from Java source code

This sample code is intended to be used to show how to debug Java application built by native-image using GraalVM VSCode Extensin Pack for Java.

## Prerequisities
1. Linux OS with __GDB 10.1__
1. VSCode has to be installed
2. Then go to VSCode Extensions activity panel and search for _GraalVM Extension Pack for Java_, install it. The GraalVM Extension Pack will install all required extensions.
3. Navigate to __Gr__ activity panel in VSCode and install some of the latest GraalVM __Enterprise Edition__ from the list.
4. Install also Native Image component

## Building the Sample
In VSCode terminal run following command ```$mvn -Pnative -DskipTests package```

## Debugging the Sample
1. Select Run and Debug activity panel in VSCode
2. Add new launch configuration named ```Native Image: launch``` into launch.json which should look like this:
```
{
    "type": "nativeimage",
    "request": "launch",
    "name": "Launch Native Image",
    "nativeImagePath": "${workspaceFolder}/target/javagdb",
    "args": "100"
}
```
3. Then run debugger using ```Launch Native Image``` from RUN ... menu and it will start debugging native image binary in VSCode using Java source code.        
