FROM gradle:8.7-jdk21 AS build
WORKDIR /app

COPY . .
RUN ./gradlew --no-daemon shadowJar

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/build/libs/app.jar /app/app.jar

EXPOSE 8096

CMD ["java", "-jar", "/app/app.jar"]
