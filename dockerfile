FROM openjdk:17

WORKDIR /opt/app
COPY target/test_task_mega-0.0.1-SNAPSHOT.jar ./test_task_mega-0.0.1-SNAPSHOT.jar
COPY pom.xml .
COPY src ./src

EXPOSE 8080

CMD ["java", "-jar", "test_task_mega-0.0.1-SNAPSHOT.jar"]
