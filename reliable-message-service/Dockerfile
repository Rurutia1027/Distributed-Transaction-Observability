# Two-Phase Reliable Message Service (Spring Boot + gRPC)
# Build from repo root: reliable-message-service/

FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /build

COPY pom.xml .
COPY message-service-api/pom.xml message-service-api/
COPY message-service/pom.xml message-service/
COPY message-service-api/src message-service-api/src
COPY message-service/src message-service/src

RUN mvn -B -q clean package -DskipTests -pl message-service -am

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -S rms && adduser -S rms -G rms
USER rms

COPY --from=build /build/message-service/target/message-service-*.jar /app/app.jar

EXPOSE 8080 9090

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
