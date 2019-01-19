FROM openjdk:8-jdk-alpine AS builder

WORKDIR /workspace
COPY . /workspace
RUN ./gradlew distTar



FROM openjdk:8-jre-alpine

ARG VERSION="1.0-SNAPSHOT"

WORKDIR /app
COPY --from=builder /workspace/build/distributions/kocam-${VERSION}.tar /app/application.tar
RUN tar xf application.tar \
    && mv /app/kocam-${VERSION}/* ./ \
    && rm -rf application.tar kocam-${VERSION}

ENV THRESHOLD 1
ENV CAMERA_IMAGE_URL ""
ENV DATA_DIRECTORY "/data"
ENV INTERVAL 5000
ENV LOG_LEVEL "INFO"

VOLUME /data

CMD "/app/bin/kocam"
