FROM openjdk:17-jdk-alpine

LABEL authors="Sardor_Boboyev1"

COPY build/libs/StoryService-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]