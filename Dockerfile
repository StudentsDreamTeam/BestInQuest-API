FROM openjdk:21-jdk
LABEL org.opencontainers.image.authors="StudentsDreamTeam"
EXPOSE 15614
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application.properties ./config/application.properties
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=classpath:/,file:./config/application.properties"]
