FROM openjdk:21-jdk
LABEL org.opencontainers.image.authors="StudentsDreamTeam"
EXPOSE 15614
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
