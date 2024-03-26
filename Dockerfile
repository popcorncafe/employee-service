FROM eclipse-temurin:21-jdk-alpine as build-jre

WORKDIR /opt

COPY jdk-modules.info .

RUN jlink \
             --add-modules $(cat jdk-modules.info) \
             --compress 2 \
             --no-header-files \
             --no-man-pages \
             --strip-debug \
             --output jdk

FROM alpine:3.19

VOLUME /tmp

ENV EXTRACTED_JAR_PATH=extracted-jar
ENV APPLICATION_USER=spring-app

ENV JAVA_HOME=/jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER

RUN mkdir /app && \
    chown -R $APPLICATION_USER /app

USER 1000

WORKDIR /app

COPY --from=build-jre opt/jdk $JAVA_HOME
COPY $EXTRACTED_JAR_PATH/dependencies/ ./
COPY $EXTRACTED_JAR_PATH/spring-boot-loader/ ./
COPY $EXTRACTED_JAR_PATH/application/ ./

HEALTHCHECK --interval=5s --timeout=5s --retries=10 CMD wget -qO- http://localhost:8091/actuator/health/ | grep UP || exit 1

EXPOSE 8091

ENTRYPOINT ["java", "-cp", "/app", "org.springframework.boot.loader.launch.JarLauncher"]