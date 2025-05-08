FROM openjdk:21-jdk-alpine
LABEL org.opencontainers.image.authors="StudentsDreamTeam"
EXPOSE 15614
ARG JAR_FILE=target/name.jar
COPY ${JAR_FILE} name.jar
ENTRYPOINT ["java","-jar","/name.jar"]
