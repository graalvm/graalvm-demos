ARG FULL_GRAALVM_VERSION

FROM findepi/graalvm:${FULL_GRAALVM_VERSION} as graalvm-jdk-image

# Install wget in the slim image
RUN apt-get update && apt-get install -yq --no-install-recommends wget pv time

# Set up GraalVM
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

ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
ENV JDK8_HOME="${JAVA_HOME}"
RUN echo; echo "JDK8_HOME=${JDK8_HOME}"; echo
RUN echo; echo "PATH=${PATH}"; echo
RUN echo " --- Java version:"; ${JDK8_HOME}/bin/java -version; echo

# Install mvn
ARG MAVEN_VERSION
RUN cd /tmp
RUN wget https://raw.githubusercontent.com/Drambluker/install-maven/main/install-maven.sh
RUN wget https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | pv -l >/dev/null
RUN chmod +x ./install-maven.sh
RUN ./install-maven.sh -f apache-maven-${MAVEN_VERSION}-bin.tar.gz | pv -l >/dev/null
ENV M2_HOME="/usr/local/apache-maven/apache-maven-${MAVEN_VERSION}/"
ENV PATH=${M2_HOME}/bin:${PATH}
RUN echo " --- Maven version:"; mvn --version; echo

LABEL maintainer="GraalVM team"
LABEL example_git_repo="https://github.com/graalvm/graalvm-demos"
LABEL graalvm_version=${FULL_GRAALVM_VERSION}
LABEL version=${FULL_GRAALVM_VERSION}