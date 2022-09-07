FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle installDist --no-daemon

FROM openjdk:11
EXPOSE 9090:9090
RUN mkdir /app
COPY --from=build /home/gradle/src/build/install/Full-Stack-Shopping-List /app/shopping-list
ENTRYPOINT ["/app/shopping-list/bin/Full-Stack-Shopping-List"]