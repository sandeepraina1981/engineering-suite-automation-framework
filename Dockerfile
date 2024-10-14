FROM alpine:latest

RUN apk update && \
    apk add --no-cache openjdk21 maven git openssh-client

ARG JAVA_HOME=/usr/lib/jvm/java-21-openjdk
ARG MAVEN_HOME=/root/.m2
ARG PATH="$JAVA_HOME/bin:$MAVEN_HOME:$PATH"
ARG LENZE=/lenze
ARG SUITE=$LENZE/suite
ARG FRAMEWORK=$SUITE/testframework

ENV GIT_ORGANIZATION=NUPANO
ENV GIT_REPOSITOARY=nupano_suite_testautomation
ENV GIT_BRANCH=main
ENV TEST_LABELS=regression
ENV TEST_URL=https://www.google.com

RUN mkdir -m 777 -p $FRAMEWORK
RUN mkdir -m 777 -p $LENZE
RUN mkdir -m 777 -p $SUITE
RUN mkdir -m 777 -p $MAVEN_HOME

RUN mkdir /root/.ssh && chmod -R 700 /root/.ssh
ADD .ssh/ /root/.ssh
RUN chmod 0400 /root/.ssh/id_rsa

ADD pom.xml $SUITE
ADD settings.xml $MAVEN_HOME/settings.xml

WORKDIR $SUITE
RUN mvn verify -DskipTests

WORKDIR $FRAMEWORK
VOLUME $FRAMEWORK

CMD ["sh", "-c", "ssh-keyscan github.com >>/root/.ssh/known_hosts && \
                  git clone git@github.com:$GIT_ORGANIZATION/$GIT_REPOSITOARY.git . && \
                  mvn -o verify -Dlabel=$TEST_LABELS -Dsuite_url=$TEST_URL"]