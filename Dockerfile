FROM openjdk:11.0.9-jdk-slim
ARG BASE_VERSION=0.0.1-SNAPSHOT
WORKDIR /usr/src/app
COPY target/demo-pug-spring-${BASE_VERSION}.jar ./demo-pug-spring.jar
CMD java -jar demo-pug-spring.jar
