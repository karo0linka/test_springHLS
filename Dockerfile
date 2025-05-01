FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw .
RUN chmod +x mvnw

COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw package -DskipTests

EXPOSE 8080

ENV SPRING_PROFILE=no-db

CMD ["sh", "-c", "./mvnw spring-boot:run -Dspring-boot.run.profiles=${SPRING_PROFILE}"]
