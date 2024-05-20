FROM openjdk:21-jdk

WORKDIR /app

COPY target/DisplaySystemSpringboot-0.0.1-SNAPSHOT.jar app.jar

ENV MONGODB_URI=mongodb://localhost:27017/displayDB

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
