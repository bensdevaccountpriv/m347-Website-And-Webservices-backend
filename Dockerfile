# Use a Gradle image that ALREADY has Java 21
FROM gradle:8-jdk21 AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME=/home/gradle/cache_home
COPY --chown=gradle:gradle build.gradle.* gradle.properties settings.gradle.* /home/gradle/app/
COPY --chown=gradle:gradle gradle /home/gradle/app/gradle
WORKDIR /home/gradle/app
RUN gradle dependencies --no-daemon

FROM gradle:8-jdk21 AS build
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon
# Stage 3: Create the Runtime Image
FROM amazoncorretto:22 AS runtime
EXPOSE 8080
RUN mkdir /app
# Use a wildcard (*) to find the jar regardless of the name
COPY --from=build /home/gradle/src/build/libs/*.jar /app/application.jar
ENTRYPOINT ["java","-jar","/app/application.jar"]
