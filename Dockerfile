FROM openjdk:17-jdk-alpine
COPY target/*.jar healthMonitor.jar
ENTRYPOINT ["java","-jar","./healthMonitor.jar"]
