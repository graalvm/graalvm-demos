ARG FULL_GRAALVM_VERSION

FROM findepi/graalvm:${FULL_GRAALVM_VERSION} as graal-jdk-image

ARG GRAALVM_HOME
ENV GRAALVM_HOME=${GRAALVM_HOME}
RUN echo "GRAALVM_HOME=${GRAALVM_HOME}"
RUN echo " --- GraalVM version (runtime)"; ${GRAALVM_HOME}/bin/java -version

# Install some of the needed components using 'gu install'
RUN echo " --- Downloading component 'espresso' using gu"; gu install espresso

# Install Java 8
COPY --from=java:8u111-jdk /usr/lib/jvm /usr/lib/jvm

ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
ENV PATH=${JAVA_HOME}/bin:${PATH}

RUN echo "JAVA_HOME=${JAVA_HOME}"
RUN echo "PATH=${PATH}"
RUN echo " --- Java version:"; java -version
ENV JDK8_HOME="${JAVA_HOME}"
RUN echo "JDK8_HOME=${JDK8_HOME}"

ARG IMAGE_VERSION
LABEL maintainer="GraalVM"
LABEL example_git_repo="https://github.com/graalvm/graalvm-demos"
LABEL graalvm_version=${FULL_GRAALVM_VERSION}
LABEL version=${IMAGE_VERSION}