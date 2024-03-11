FROM openjdk:21-slim

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} configserver.jar

EXPOSE 9296

CMD ["java", "-jar", "/configserver.jar"]