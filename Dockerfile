FROM  alpine:latest AS build
WORKDIR /app
RUN apk update  && apk add wget maven
RUN wget -P /etc/apk/keys/ https://cdn.azul.com/public_keys/alpine-signing@azul.com-5d5dc44c.rsa.pub
RUN echo "https://repos.azul.com/zulu/alpine" | tee -a /etc/apk/repositories
RUN apk update && apk add zulu21-jdk binutils
RUN mkdir skibidichat
COPY ./src ./skibidichat/src
COPY pom.xml ./skibidichat/
RUN cd skibidichat && mvn clean package
RUN jlink --add-modules java.base,java.compiler,java.desktop,java.instrument,java.logging,java.management,java.naming,java.rmi,java.security.jgss,java.security.sasl,java.sql,java.transaction.xa,java.xml --output custom-jre --strip-debug --no-man-pages --no-header-files --compress=zip-7


FROM alpine:latest AS run
WORKDIR /app
COPY --from=build /app/custom-jre /opt/java
COPY --from=build /app/skibidichat/target/fat-jar-with-dependencies.jar .
ENV JAVA_HOME=/opt/java
ENV PATH=$JAVA_HOME/bin:$PATH