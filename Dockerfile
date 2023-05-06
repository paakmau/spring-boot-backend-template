FROM eclipse-temurin:17-jdk-alpine
COPY build/libs/demo-*.jar demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]
