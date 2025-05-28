FROM amazoncorretto:21
ARG JAR_FILE=target/ewm-service-0.0.1-SNAPSHOT.jar
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
