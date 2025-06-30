FROM openjdk:21-slim

WORKDIR /app

COPY target/loan-simulator-*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]