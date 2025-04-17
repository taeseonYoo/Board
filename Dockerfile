FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY build/libs/board-0.0.1-SNAPSHOT.jar board-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java","-jar","board-0.0.1-SNAPSHOT.jar"]