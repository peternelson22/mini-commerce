FROM openjdk:21-slim

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} serviceregistry.jar

EXPOSE 8761

CMD ["java", "-jar", "/serviceregistry.jar"]