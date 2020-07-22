# Installing GraalVM Enterprise Edition and Setup

This guide, will walk you through the process of getting setup with GraalVM. This guide is written to install GraalVM version 20.1.1. 

## Notes on Setting up on the Mac
These instructions, currently, are focused on installing on Linux and Mac. If youy install on a Mac, a number of the details will vary a little from the general approach. Where this is the case I will document them and highlight.

It is worth pointing out these differences at the start as well, though.

First, if you place the GRAAL_HOME to /Library/Java/JavaVirtualMachines/, MAC OS picks the JDK up as default-jdk (as long it’s the newest JDK in the directory). You may not want to do this, I didn't. 

Secondly the path that the core GraalVM download extracts to is different from that for Linux. For Mac it adds a few directories. On Mac, the directory that will be used as your `GRAALVM_HOME` will be, relative to the directory that you extract it to:

    <install-dir>/graalvm-ee-java8-20.1.1/Contents/Home

The third major difference is that on some versions of OSX the `GateKeeper` service on OSX will block you from running `GraalVM` as it is not a signed application / binary. This can be worked around in a number of ways:

* Disabling `GateKeeper`, running and then reenabling
* On latter versions of OSX, you will get prompted that it is blocked and `GateKeeper` will prompt you to add an exception for it
* You can add make a command line app allowed by using the `spctl` tool:

    ~~~ {.bash}
    # Run from the terminal
    # This assumes that you install /Library/Java/...
    # Please update if you choose to install in another location
    sudo spctl add $GRAALVM/bin/java
    ~~~

## Downloading GraalVM, Enterprise Edition 20.1.1

It is important to state that are there are two versions of `GraalVM`, the Enterprise Edition (supported and with better performance) and the Community Edition (free and Open Source).

Both of these can be dwonloaded from the GraalVM website, but my instrcutions here will detail downloading, and then install the Enterprise Edition.

The download for the Enterprise Edition can be found at: [https://www.oracle.com/downloads/graalvm-downloads.html?selected_tab=20](https://www.oracle.com/downloads/graalvm-downloads.html?selected_tab=20)

You should see the following when you go to the Download Page:

![GraalVM Download Page](./images/download-page-01.png)

Click on the Current Release tab on the page, see image below, to display the download links for the current version. Make sure that you select the correct current minor version for the current release (the default should be selected already), the correct JDK version (8 or 11? Or both?) and the OS. Again these are all are high-lighted on the image below. The click on the "Download All" button.

![GraalVM Download Page - Select Current Verion, JDK and OS](./images/download-page-02.png)

You will be asked to accept the license agreement (OTN license, this means that you can use GraalVM EE for evaulation and devleopment purposes). It will then ask you to log into Oracle. If you have an Oracle OTN account then use this identity to download GraalVM. If not, create one by hitting the "Create Acount" button at the bottom opf the page. This is shown below:

![GraalVM Download Page - Login](./images/download-page-03.png)

After downloading the Core, which is the steps above, you can download any of the following optional package / languages for GraalVM. All of these are available form the page on whih we downloaded the core:

Please download the following optional packages:

* Oracle GraalVM Enterprise Edition Native Image
* GraalVM LLVM Toolchain Plugin

Optional plugins for GraalVM: 

* Ideal Graph Visualizer
* Oracle GraalVM Enterprise Edition Ruby Language Plugin
* Oracle GraalVM Enterprise Edition Python Language Plugin
* Oracle GraalVM Enterprise Edition WebAssembly Language Plugin

## Installing GraalVM

The full instructions on getting setup, can be found here, [Installing ](https://www.graalvm.org/docs/getting-started/#install-graalvm).

A quick summary of the steps outlined in the link above are:

1. Download the latest version of GraalVM EE for your OS, from [Download](https://www.oracle.com/downloads/graalvm-downloads.html)
    - See earlier section, Downloading GraalVM
2. You will need to download the following packages:
    - Oracle GraalVM Enterprise Edition for JDK8 (Version 20.1.1)
    - Oracle GraalVM Enterprise Editipon Native Image Early Adopter based on JDK8 (Version 20.1.1)
    - GraalVM LLVM Toolchain Plugin (Version 20.1.1)
    - Oracle GraalVM Enterprise Edition Python for JDK8 (Version 20.1.1)
    - Oracle GraalVM Enterprise Edition Ruby for JDK8 (Version 20.1.1)
3. Install the downloaded GraalVM EE core. This is a `tar.gz` file. You will need to extract it to a location that works for you and that will then become the root of your install. I chose the following: `~/bin/graal/graalvm-ee-java8-20.1.1`
4. Update your environment in order to add a `GRAALVM_HOME` and add this to the path. You can do this by updating your shell startup script, in my case  `~/.zshrc` as follows:

    ~~~ {.bash}
    # Note that I am using the JDK8 version.
    export GRAALVM_HOME=~/bin/graal/graalvm-ee-java8-20.1.1
    export JAVA_HOME="${GRAALVM_HOME}"
    # Add the bin dir of GraalVM to your path, so you will be able to reference the exes
    export PATH="${GRAALVM_HOME}/bin:$PATH"
    ~~~

#### Installing on Mac

On Mac, when you extract the download for GraalVM, the core download, not the language packages, the path is slightly different to what it is on Linux. For me, using a Mac, I have the following setup:

~~~ {.bash}
# Note that I am using the JDK8 version.
export GRAALVM_HOME=~/bin/graal/graalvm-ee-java8-20.1.1/Contents/Home
export JAVA_HOME=${GRAALVM_HOME}
# Add the bin dir of GraalVM to your path, so you will be able to reference the exes
export PATH=${GRAALVM_HOME}/bin:$PATH
~~~

**NB**: Note that the `Home` directory for GraalVM contains extra directories from what it does on Linux, `Contents/Home`.

### Testing Your Installation

Create a new shell (or source your shell script) and type the following:

~~~ {.bash}
java -version
java version "1.8.0_261"
Java(TM) SE Runtime Environment (build 1.8.0_261-b33)
Java HotSpot(TM) 64-Bit Server VM GraalVM EE 20.1.1 (build 25.261-b33-jvmci-20.1-b04, mixed mode)
~~~

> #### Mac - GateKeeper
> On OSX you may see a pop-up warning you that the `java` is not a signed application and can not be run. This is not something to worry about, it is just OSX being over protectve :)
> See the earlier section of this page on the Mac specific steps on installing GraalVM (you need to let the `GateKeeper` service know that GraalVm is safe to be run).

Did you see the same output as above? If so, then it worked.

Test out the `gu` package tool:

~~~ {.bash}
$ gu --help
~~~

Did that work? If it did you now have access to the package tool that will allow you to install the various additional packages that come with GraalVM. We will go through a few of these shortly.

### Installing Language Modules

We use the `gu` package tool to install the extra additional modules that you can use with GraalVm, such as the various language runtimes. In this section we will step through installing these.

All language / component installations are carrid out using the `gu` tool that is distributed with GraalVM. The latest instructions on using this toll to install components can be found at:

[gu Tool - Enterprise Edition](https://docs.oracle.com/en/graalvm/enterprise/20/guide/reference/graalvm-updater.html)

#### Installing JS / Node

Nothing to do here, these are available by default.

You can test the `node` and `JS` tooling as follows:

~~~ {.bash}
$ node --help
$ js --show-version
~~~

Both of the above commands will make reference to graalvm and the version. If you have node already installed, you may need to change your path or explicitly specify the path to the GraalVm version of node

### Installing the llvm-toolchain

A number of the packages require the `llvm-toolchain` in order to work. Regardless of whether you are using the Enterprise, or Community, Edition you will need to install the same version of this.

This is installed simply, using the `gu` command:

~~~ {.bash}
$ gu install -L <DOWNLOAD-DIR>/llvm-toolchain-installable-java8-darwin-amd64-20.1.1.jar
~~~

#### Installing Native Image

Full instructions can be found [here](https://www.graalvm.org/docs/reference-manual/native-image/#install-native-image), but the process is very similar to that for Python.

1. Ensure you have the prerequisite libs available on your system: `glibc-devel, zlib-devel`
    - On linux these can be installed using your package manager
    - On OSX you will need to ensure that
2. `$ gu install -L <DOWNLOAD-DIR>/native-image-installable-svm-svmee-java8-darwin-amd64-20.1.1.jar`
3. Test the installation with: `native-image --version`

### Intalling Ruby

Full instructions can be found [here](https://www.graalvm.org/docs/reference-manual/languages/ruby/#installing-ruby).

But the basic steps are:

1. `$gu install -L <DOWNLOAD-DIR>/ruby-installable-svm-svmee-java8-linux-amd64-20.1.1.jar`

Test that your installation works by running ruby:

~~~ {.bash}
$ which ruby
$ ruby --version
~~~

If you already have Ruby installed, then you will need to adjust your path or use the full path to the ruby binary.

#### Installing Python

Full instructions can be found [here](https://www.graalvm.org/docs/reference-manual/languages/python/#installing-python).

But the basic steps are:

1. `$ gu -L install <DOWNLOAD-DIR>/python-installable-svm-svmee-java8-linux-amd64-20.1.1.jar`
2. Test that Python is now installed:

~~~ {.bash}
$ gu list
# You should see that Python EE version 20.1.1 is now installed
$ graalpython
~~~

#### Installing R

Full instructions can be found [here](https://www.graalvm.org/docs/reference-manual/languages/r/#installing-r).

When installing the R language module, it is best to consult the installation page, above, as thwere are a number of prerequisites that need to be installed.

### Install Visual Studio Code

A number of plugins ahve been written for this editor, that allow for better integration with the GraalVM eco-system.

You are more than welcome to use another editor. When it comes to the polyglot debugging, please make sure that you have the Google Chrome browser installed.

### Install VS Code Plugins

Please install the following plugins:

1. GraalVM Extension Pack

This will install all of the individual extensions

## Docker - Pre-built Docker Images

GraalVM is available on a number of pre-built docker images that can be downloaded from Docker Hub. Currently only the Community Edition is available hee, but we will be making the Enterprise Edition available through Docker Hub soon.

You can find the Docker Images [here](https://hub.docker.com/r/oracle/graalvm-ce).



