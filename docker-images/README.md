# Running GraalVM Demos in a Docker Container

Some of the demos (console-based, non-GUI) can be run inside the confinement of a Docker container.

To get started, clone or download GraalVM Demos repository: `git clone https://github.com/graalvm/graalvm-demos.git`. After cloning the repository, but before running any of the demos, do the following.

1. Build the GraalVM demo Docker image of choice:
    ```
    cd docker-images
    ./buildDockerImage.sh "java11-22.2.0"
    ```

    Note: You can find valid tags to specify as parameters [here](https://github.com/graalvm/container/pkgs/container/graalvm-ce).

2. Return to the root directory of the project and run the GraalVM demo Docker container built above:
    ```
    ./runDockerImage.sh "java11-22.2.0"
    ```

Once the Docker container is running, go to the folder of the respective demo, and follow the instructions from its README.md.
Note that GraalVM runtime built with Docker will already contain additional GraalVM components such as Node.JS, Ruby, Python etc., required to run some of the demos.

Running GUI-based applications inside a Docker container requires some intermediate VNC Viewer to access the GUI interface.

1. Download and install any **VNC Viewer**. A number of free or commercial VNC Viewers can be found [here](https://duckduckgo.com/?q=vnc+viewer+download&ia=web).
2. Wait for the container to be ready, then run VNC Viewer.
3. Log onto http://127.0.0.1:5900 (type it in, in case copy-paste does not work) via the VNC Viewer to access the GUI interface. You will get an `xterm` screen, where you can type in your commands just like the Docker console or any other CLI prompt.

Finally, from the root directory of this repository, run a Docker container with the GraalVM runtime in it:
  ```
  DEMO_TYPE="gui" ./runDockerImage.sh "java11-22.2.0"
  ```

Please note that GraalVM demos are regularly [tested against the latest GraalVM using GitHub Actions](https://github.com/graalvm/graalvm-demos/actions/workflows/main.yml), but not in a Docker container. If you come accross an issue, please submit it [here](https://github.com/graalvm/graalvm-demos/issues).