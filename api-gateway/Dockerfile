FROM openjdk:21-slim

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} apigateway.jar

EXPOSE 9090

CMD ["java", "-jar", "/apigateway.jar"]