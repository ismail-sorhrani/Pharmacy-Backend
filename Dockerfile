FROM openjdk:17-jdk-alpine
WORKDIR /backend
COPY target/app.jar .
EXPOSE 9090
ENTRYPOINT ["java", "-jar","app.jar"]