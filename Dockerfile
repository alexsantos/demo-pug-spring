FROM openjdk:11.0.9-jdk-slim
ENV VERSION 0.1.2-SNAPSHOT
WORKDIR /usr/src/app
COPY target/demo-pug-spring-${VERSION}.jar .
CMD java -jar demo-pug-spring-${VERSION}.jar