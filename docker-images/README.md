# Running GraalVM Demos in a Docker Container

Some of the demos (console-based, non-GUI) can be run inside a Docker container.

To get started, clone or download GraalVM Demos repository: `git clone https://github.com/graalvm/graalvm-demos.git`. After cloning the repository, but before running any of the demos, perform the following steps.

1. Build your GraalVM demo Docker image of choice:
    ```
    cd docker-images
    ./buildDockerImage.sh "java11-22.2.0"
    ```

    > Note: You can find valid tags to specify as parameters [here](https://github.com/graalvm/container/pkgs/container/graalvm-ce).

2. Return to the root directory of the project and run the GraalVM demo Docker image built above:
    ```
    ./runDockerImage.sh "java11-22.2.0"
    ```

Once the Docker container is running, go to the directory of the respective demo, and follow the instructions from its _README.md_ file.
Note that GraalVM runtime built with Docker will already contain additional GraalVM components (such as Node.JS, Ruby, and Python) required to run some of the demos.

Running GUI-based applications inside a Docker container requires a VNC Viewer to access the GUI interface. The following steps describe how to use a VNC Viewer.

1. Download and install any **VNC Viewer**. Free and commercial VNC Viewers can be found [here](https://duckduckgo.com/?q=vnc+viewer+download&ia=web).
2. Wait for the container to be ready, then run VNC Viewer.
3. Log in to http://127.0.0.1:5900 (enter the URL, in case copy-paste does not work) via the VNC Viewer to access the GUI interface. You will see an `xterm` screen, where you can enter your commands in the same way that you use the Docker console or any other CLI prompt.

Finally, from the root directory of this repository, run a Docker container containing the GraalVM runtime:
  ```
  DEMO_TYPE="gui" ./runDockerImage.sh "java11-22.2.0"
  ```

Please note that GraalVM demos are regularly [tested against the latest GraalVM using GitHub Actions](https://github.com/graalvm/graalvm-demos/actions/workflows/main.yml), but not in a Docker container. If you come accross an issue, please submit it [here](https://github.com/graalvm/graalvm-demos/issues).