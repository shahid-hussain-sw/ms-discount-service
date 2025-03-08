# Build stage
FROM gradle:8.7-jdk-21-and-22-alpine AS build
WORKDIR /app
COPY build.gradle .
COPY src ./src
RUN gradle clean build

FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=build /app/build/ms-discount-service-1.0.0 app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]