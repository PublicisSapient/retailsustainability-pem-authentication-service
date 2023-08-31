FROM openjdk:17
VOLUME /tmp
ARG JAR_FILE=target/authentication-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]