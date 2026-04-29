FROM gradle:8.7-jdk21 AS build
WORKDIR /app
COPY backend/ .
RUN ./gradlew shadowJar

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
EXPOSE 8096
CMD ["java", "-jar", "app.jar", "--read-courses", "--read-rmp", "--db-type=sqlite"]