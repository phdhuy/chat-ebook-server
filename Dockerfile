# Build stage
FROM maven:3.8-openjdk-17 AS build
WORKDIR /usr/src/app

COPY pom.xml .
COPY common/pom.xml ./common/
COPY file/pom.xml ./file/
COPY mail/pom.xml ./mail/
COPY web/pom.xml ./web/
COPY security/pom.xml ./security/
COPY chat/pom.xml ./chat/
COPY ai/pom.xml ./ai/
COPY mindmap/pom.xml ./mindmap/
COPY subscription/pom.xml ./subscription/
COPY feedback/pom.xml ./feedback/


RUN mvn dependency:go-offline -B

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:19-alpine
EXPOSE 8081
WORKDIR /usr/app
COPY --from=build /usr/src/app/web/target/web-0.0.1-SNAPSHOT.jar /usr/app
CMD ["java", "-jar", "web-0.0.1-SNAPSHOT.jar"]