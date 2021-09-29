ARG FULL_GRAALVM_VERSION

FROM findepi/graalvm:${FULL_GRAALVM_VERSION} as graalvm-jdk-image

# Install smaller utilities needed during building of image in the slim image
RUN echo; echo "--- Installing wget, curl, vim, unzip in the slim image"; echo
RUN apt-get update && apt-get install -yq --no-install-recommends wget curl vim unzip

# Install wrk in the slim image, see https://github-wiki-see.page/m/giltene/wrk2/wiki/Installing-wrk2-on-Linux
RUN echo; echo "--- Installing wrk: workload generator (multiple threads)"; echo
RUN apt-get install -yq --no-install-recommends build-essential libssl-dev git
RUN cd /tmp/ && git clone https://github.com/wg/wrk.git
RUN cd /tmp/wrk && make
RUN chmod +x /tmp/wrk/wrk && cp /tmp/wrk/wrk /usr/local/bin

# Setup GraalVM paths
ARG GRAALVM_HOME
ENV GRAALVM_HOME=${GRAALVM_HOME}
ENV JAVA_HOME=${GRAALVM_HOME}
ENV PATH=${GRAALVM_HOME}/bin:${PATH}
RUN echo; echo "JAVA_HOME=${GRAALVM_HOME}"; echo
RUN echo; echo "GRAALVM_HOME=${GRAALVM_HOME}"; echo
RUN echo; echo " --- GraalVM version (runtime)"; java -version; echo

# Install some of the needed components using 'gu install'
RUN echo; echo " --- Downloading & install 'espresso' using gu"; gu install espresso; echo
RUN echo; echo " --- Downloading & install 'nodejs' using gu"; gu install nodejs; echo
RUN echo; echo " --- Downloading & install 'python' using gu"; gu install python; echo

# Install Java 8
COPY --from=java:8u111-jdk /usr/lib/jvm /usr/lib/jvm

ENV JDK8_HOME="/usr/lib/jvm/java-1.8.0-openjdk-amd64"
RUN echo; echo "JDK8_HOME=${JDK8_HOME}"; echo
RUN echo; echo "PATH=${PATH}"; echo
RUN echo " --- Java version:"; ${JDK8_HOME}/bin/java -version; echo

# Install mvn
ARG MAVEN_VERSION
RUN cd /tmp
RUN wget -q -nv https://raw.githubusercontent.com/Drambluker/install-maven/main/install-maven.sh
RUN wget -q -nv "https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz"
RUN chmod +x ./install-maven.sh
RUN ./install-maven.sh -f apache-maven-${MAVEN_VERSION}-bin.tar.gz
ENV M2_HOME="/usr/local/apache-maven/apache-maven-${MAVEN_VERSION}/"
ENV PATH=${M2_HOME}/bin:${PATH}
RUN echo " --- Maven version:"; mvn --version; echo

LABEL maintainer="GraalVM team"
LABEL example_git_repo="https://github.com/graalvm/graalvm-demos"
LABEL graalvm_version=${FULL_GRAALVM_VERSION}
LABEL version=${FULL_GRAALVM_VERSION}