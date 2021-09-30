ARG FULL_GRAALVM_VERSION

FROM findepi/graalvm:${FULL_GRAALVM_VERSION} as graalvm-jdk-image

COPY --from=micronaut/micronaut-starter /root/.micronaut/micronaut-cli /root/.micronaut/micronaut-cli
ENV MICRONAUT_HOME="/root/.micronaut/micronaut-cli"
ENV PATH=${MICRONAUT_HOME}/bin:${PATH}
RUN echo; echo " --- Micronaut version"; mn --version; echo

COPY --from=workload-generator/wrk /tmp/wrk/wrk /usr/local/bin
RUN echo "Testing 'wrk':"; wrk || true

# Install other smaller utilities needed during building of image in the slim image
RUN echo; echo "--- Installing wget, curl, vim, unzip in the slim image"; echo
RUN apt-get update && apt-get install -yq --no-install-recommends \
                      wget curl vim unzip gnupg2

# Install scala and sbt
# See https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html
RUN echo; echo "--- Installing scala and sbt in the slim image"; echo
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add
RUN apt-get update && apt-get install -yq --no-install-recommends scala sbt
RUN echo "Scala version:"; scala -help; echo
RUN echo "sbt version:"; sbt --version; echo
ENV SCALA_HOME="/usr/share/scala" # this maps to the specific vesion of Scala for e.g. scala-2.11
RUN echo; echo "SCALA_HOME=${SCALA_HOME}"; echo

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

# Install jmeter
RUN cd /tmp/; wget -q -nv https://dlcdn.apache.org//jmeter/binaries/apache-jmeter-5.4.1.zip
RUN unzip /tmp/apache-jmeter-5.4.1.zip
ENV JMETER_HOME="${WORKDIR}/apache-jmeter-5.4.1"
ENV PATH=${JMETER_HOME}/bin:${PATH}
RUN echo " --- Jmeter version:"; jmeter --version; echo


LABEL maintainer="GraalVM team"
LABEL example_git_repo="https://github.com/graalvm/graalvm-demos"
LABEL graalvm_version=${FULL_GRAALVM_VERSION}
LABEL version=${FULL_GRAALVM_VERSION}