FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} mobile-de-data-api.jar
ENTRYPOINT ["java","-jar","/mobile-de-data-api.jar"]