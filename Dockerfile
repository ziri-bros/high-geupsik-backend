FROM adoptopenjdk/openjdk11
EXPOSE 8080
ARG APP_JAR=*.jar
ARG PROJECT_DIR=/build/libs/
WORKDIR ${PROEJCT_DIR}
COPY ${APP_JAR} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]