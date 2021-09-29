FROM buildpack-deps:stretch-scm

# Install Java 8
COPY --from=java:8u111-jdk /usr/lib/jvm /usr/lib/jvm

ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
ENV PATH=${JAVA_HOME}/bin:${PATH}

RUN echo "JAVA_HOME=${JAVA_HOME}"
RUN echo "PATH=${PATH}"

RUN java -version
RUN echo "JAVA_HOME=${JAVA_HOME}"
ENV JAVA_8_HOME="${JAVA_HOME}"
RUN echo "JAVA_8_HOME=${JAVA_8_HOME}"

### GraalVM installation

RUN apt-get update && apt-get install pv

ENV GRAALVM_VERSION="19.0.0"
RUN wget https://github.com/oracle/graal/releases/download/vm-${GRAALVM_VERSION}/graalvm-ce-linux-amd64-${GRAALVM_VERSION}.tar.gz --progress=bar:force 2>&1 | tail -f -n +3
RUN tar xvzf graalvm-ce-linux-amd64-${GRAALVM_VERSION}.tar.gz | pv -l >/dev/null

RUN mv graalvm-ce-${GRAALVM_VERSION}/ /usr/lib/jvm/graalvm-ce-${GRAALVM_VERSION}
ENV GRAALVM_HOME="/usr/lib/jvm/graalvm-ce-${GRAALVM_VERSION}"
RUN echo "GRAALVM_HOME=${GRAALVM_HOME}"
RUN ${GRAALVM_HOME}/bin/java -version
RUN rm graalvm-ce-linux-amd64-${GRAALVM_VERSION}.tar.gz

ENV NVM_DIR /root/.nvm
ENV NVM_VERSION 0.33.11
ENV NODE_VERSION 6.1.0

RUN mkdir -p ${NVM_DIR}

# Install nvm with node and npm
RUN curl https://raw.githubusercontent.com/creationix/nvm/v${NVM_VERSION}/install.sh | bash \
    && . ${NVM_DIR}/nvm.sh \
    && nvm install ${NODE_VERSION} \
    && nvm alias default ${NODE_VERSION} \
    && nvm use default \
    && npm install -g npm

ENV NODE_MODULES_PATH ${NVM_DIR}/v${NODE_VERSION}/lib/node_modules
ENV NODE_PATH ${NVM_DIR}/versions/node/v${NODE_VERSION}
ENV PATH      ${NODE_PATH}/bin:${NODE_MODULES_PATH}/bin:${PATH}

RUN npm --version
RUN node --version