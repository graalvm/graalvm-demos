# Building a HelloWorld Java Module into a Native Executable

The following modularized Java Hello World example

    ├── hello
    │   └── Main.java
    │       > package hello;
    │       > 
    │       > public class Main {
    │       >     public static void main(String[] args) {
    │       >         System.out.println("Hello from Java Module: "
    │       >             + Main.class.getModule().getName());
    │       >     }
    │       > }
    │
    └── module-info.java
        > module HelloModule {
        >     exports hello;
        > }

can be executed via Java using the following command:
    
    ```shell
    $ java --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule
    Hello from Java Module: HelloModule
    ```
    
With GraalVM you can **now build Java modules** into native executables, using the following syntax:
    ```shell
    $ native-image --module-path target/HelloModule-1.0-SNAPSHOT.jar --module HelloModule

    [hellomodule:10847]    classlist:     513.59 ms,  0.96 GB
    [hellomodule:10847]        (cap):     386.65 ms,  0.96 GB
    ....
    [hellomodule:10847]        image:     968.93 ms,  4.85 GB
    [hellomodule:10847]        write:     201.81 ms,  4.85 GB
    [hellomodule:10847]      [total]:  19,216.19 ms,  4.85 GB
    ```                                      
It builds the modular Java app into a native executable that can be executed with:
    
    ```shell
    $ ./hellomodule 
    Hello from Java Module: HelloModule
    ```
