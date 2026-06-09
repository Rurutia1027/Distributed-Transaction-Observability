# Build stage (no need to run mvn on the host before docker build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

# Runtime: OpenTelemetry Java Agent + Spring Boot jar
# pom.xml uses java.version=21; use 17-jdk only if you downgrade the project to Java 17
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Pin agent version for reproducible builds (override at build time if needed)
ARG OTEL_AGENT_VERSION=2.1.0
ARG OTEL_AGENT_URL=https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && mkdir -p /otel \
    && curl -fsSL -o /otel/opentelemetry-javaagent.jar "${OTEL_AGENT_URL}" \
    && apt-get purge -y curl \
    && apt-get autoremove -y \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /workspace/target/eda-design-service-1.0.0.jar app.jar

EXPOSE 8080

ENV SERVER_PORT=8080

# OTEL_SERVICE_NAME, OTEL_EXPORTER_OTLP_ENDPOINT, etc. are set in docker-compose
ENTRYPOINT ["java", "-javaagent:/otel/opentelemetry-javaagent.jar", "-jar", "/app/app.jar"]
