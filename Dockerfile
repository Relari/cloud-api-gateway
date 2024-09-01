FROM openjdk:11.0-jre-slim
EXPOSE 8091
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} cloud-api-gateway.jar
ENTRYPOINT ["java","-jar","/cloud-api-gateway.jar"]