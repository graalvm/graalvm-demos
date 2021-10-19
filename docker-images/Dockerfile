ARG DOCKER_USER_NAME
ARG SOURCE_DOCKER_HUB
ARG FULL_GRAALVM_VERSION

FROM ${SOURCE_DOCKER_HUB}:${FULL_GRAALVM_VERSION} as graalvm-jdk-image
FROM ${DOCKER_USER_NAME}/micronaut-starter:${FULL_GRAALVM_VERSION} as micronaut-starter-image
FROM ${DOCKER_USER_NAME}/workload-generator as workload-generator

FROM debian:buster

COPY --from=workload-generator /tmp/wrk/wrk /usr/local/bin
RUN echo "Testing 'wrk':"; wrk || true

# Install other smaller utilities needed during building of image in the slim image
RUN echo; echo "--- Installing wget, curl, vim, unzip in the slim image"; echo
RUN apt-get update \
    && apt-get install -yq --no-install-recommends \
                       wget curl vim unzip gnupg2 \
                       make gcc g++ libc++-dev \
                       openssl libssl-dev libcrypto++-dev libz.a \
                       locales ca-certificates
RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/*

# https://www.rosehosting.com/blog/configure-system-locale-on-debian-9/
# https://people.debian.org/~schultmc/locales.html <-- simple steps
RUN echo "--- Installing and setting locales"
RUN echo "en_US.UTF-8 UTF-8" >> /etc/locale.gen
ENV LANG="en_US.UTF-8"
ENV LANGUAGE="en_US"
RUN locale-gen
RUN echo; echo "LANG=${LANG}"; echo "LANGUAGE=${LANGUAGE}";
RUN echo; echo "List of installed locales:"; locale -a; echo;

# Install gcc and make
RUN echo "gcc version: "; gcc --version
RUN echo "make version: "; make --version

ARG GRAALVM_HOME

# Install and setup GraalVM
COPY --from=graalvm-jdk-image /opt/graalvm-* ${GRAALVM_HOME}

ENV JAVA_HOME=${GRAALVM_HOME}
ENV PATH=${JAVA_HOME}/bin:${PATH}
RUN echo; echo "JAVA_HOME=${JAVA_HOME}"; echo
RUN echo; echo " --- GraalVM version (runtime)"; java -version; echo

# Install some of the needed components using 'gu install'
RUN echo; echo " --- Download & install 'espresso' using gu"; gu install espresso; echo
RUN echo; echo " --- Download & install 'nodejs' using gu"; gu install nodejs; echo
RUN echo; echo " --- Download & install 'python' using gu"; gu install python; echo
RUN echo; echo " --- Download & install 'R' using gu"; gu install R; echo
RUN echo; echo " --- Download & install 'Ruby' using gu"; gu install ruby; echo
RUN echo; echo " --- Download & install 'native-image' using gu"; gu install native-image; echo

# Rebuild Ruby to make the Ruby openssl C extensions to work with the local system libssl (see https://github.com/oracle/truffleruby/blob/master/doc/user/installing-graalvm.md#installing-ruby-and-other-languages)
RUN echo "Rebuilding Ruby to make the Ruby openssl C extensions to work with the local system libssl"

RUN export RUBY_POST_HOOK_SCRIPT="$(find ${GRAALVM_HOME} -name *post_install_hook.sh*)";  \
   chmod +x ${RUBY_POST_HOOK_SCRIPT}; \
   bash ${RUBY_POST_HOOK_SCRIPT};
# At the moment this is a simple litmus test to verify that the above step has actually worked!
RUN echo "Installing ruby gems to verify if the above installation and rebuilding processes are working..."
RUN gem install rspec galaaz

RUN echo "gcc version: "; gcc --version
RUN echo "make version: "; make --version

# https://github.com/oracle/truffleruby/blob/master/doc/user/ruby-managers.md#chruby
RUN if [ -e "${GRAALVM_HOME}/jre" ]; then  ruby_home=$(${GRAALVM_HOME}/jre/languages/ruby/bin/ruby -e 'print RbConfig::CONFIG["prefix"]');  else ruby_home=$(${GRAALVM_HOME}/languages/ruby/bin/ruby -e 'print RbConfig::CONFIG["prefix"]'); fi


# Install Java 8
COPY --from=java:8u111-jdk /usr/lib/jvm /usr/lib/jvm

ENV JDK8_HOME="/usr/lib/jvm/java-1.8.0-openjdk-amd64"
RUN echo; echo "JDK8_HOME=${JDK8_HOME}"; echo
RUN echo; echo "PATH=${PATH}"; echo
RUN echo " --- Java 8 version:"; ${JDK8_HOME}/bin/java -version; echo

# Install mvn
ARG MAVEN_VERSION
RUN cd /tmp
RUN wget -q -nv https://raw.githubusercontent.com/Drambluker/install-maven/main/install-maven.sh
RUN wget -q -nv "https://www.mirrorservice.org/sites/ftp.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz"
RUN chmod +x ./install-maven.sh
RUN ./install-maven.sh -f apache-maven-${MAVEN_VERSION}-bin.tar.gz
ENV M2_HOME="/usr/local/apache-maven/apache-maven-${MAVEN_VERSION}/"
ENV PATH=${M2_HOME}/bin:${PATH}
RUN echo " --- Maven version:"; mvn --version; echo

# Install gradle
ARG GRADLE_VERSION
RUN cd /tmp/; wget -q -nv https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip
RUN cd /tmp/; unzip gradle-${GRADLE_VERSION}-bin.zip && mv gradle-${GRADLE_VERSION} /
ENV GRADLE_HOME="/gradle-${GRADLE_VERSION}"
ENV PATH=${GRADLE_HOME}/bin:${PATH}
RUN echo " --- Gradle version:"; gradle --version; echo

# Install jmeter
RUN cd /tmp/; wget -q -nv "https://dlcdn.apache.org//jmeter/binaries/apache-jmeter-5.4.1.zip"
RUN unzip /tmp/apache-jmeter-5.4.1.zip
ENV JMETER_HOME="${WORKDIR}/apache-jmeter-5.4.1"
ENV PATH=${JMETER_HOME}/bin:${PATH}
RUN echo " --- Jmeter version:"; jmeter --version; echo

# Install scala and sbt
ARG SBT_VERSION
RUN echo; echo "--- Installing scala and sbt in the slim image"; echo
# See https://www.scala-sbt.org/download.html
RUN cd /tmp/; wget -q -nv https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.zip
RUN cd /tmp/; unzip sbt-${SBT_VERSION}.zip; mv sbt /usr/share; ln -s /usr/share/sbt/bin/sbt /usr/bin/sbt
RUN echo "sbt version:"; sbt --version | grep "sbt script version"; echo

ARG SCALA_VERSION
# See https://www.scala-lang.org/download/
RUN cd /tmp/; wget -q -nv https://github.com/lampepfl/dotty/releases/download/${SCALA_VERSION}/scala3-${SCALA_VERSION}.zip
RUN cd /tmp/; unzip scala3-${SCALA_VERSION}.zip; mv scala3-${SCALA_VERSION} /usr/share/scala;
RUN ln -s /usr/share/scala /usr/share/scala-${SCALA_VERSION}; ln -s /usr/share/scala/bin/scala /usr/bin/scala
RUN echo "scala version:"; scala -version; echo

# this location maps to the specific vesion of Scala for e.g. scala-2.12 or 3.0.2
ENV SCALA_HOME="/usr/share/scala"
RUN echo; echo "SCALA_HOME=${SCALA_HOME}"; echo

COPY --from=micronaut-starter-image /root/.micronaut/micronaut-cli /root/.micronaut/micronaut-cli
ENV MICRONAUT_HOME="/root/.micronaut/micronaut-cli"
ENV PATH=${MICRONAUT_HOME}/bin:${PATH}
RUN echo; echo " --- Micronaut version"; mn --version; echo

# Some demo apps require this env variable set
ENV GRAALVM_HOME=${GRAALVM_HOME}
RUN echo; echo "GRAALVM_HOME=${GRAALVM_HOME}"; echo

LABEL maintainer="GraalVM team"
LABEL example_git_repo="https://github.com/graalvm/graalvm-demos"
LABEL graalvm_version=${FULL_GRAALVM_VERSION}
LABEL version=${FULL_GRAALVM_VERSION}