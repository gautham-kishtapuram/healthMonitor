FROM openjdk:17-jdk-alpine
EXPOSE 8089
COPY target/*.jar healthMonitor.jar
ENTRYPOINT ["java","-jar","./healthMonitor.jar"]
