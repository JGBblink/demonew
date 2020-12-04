# Build with: docker build -t rplus-service-community .
# Run with: docker run -p 20090:20090 -d --name rplus-service-community rplus-service-community

FROM openjdk:8-jdk-alpine3.9
ARG JAR_FILE

MAINTAINER LiuDecai liudecai.ldc@qq.com
LABEL maintainer="LiuDecai liudecai.ldc@qq.com"

WORKDIR /app

ENV JAVA_OPTS=""
ENV DEFAULT_JAVA_OPTS="\
-Xmx1g \
-Xms512m \
-Dfile.encoding=UTF-8 \
-XX:+PrintGCDetails \
-XX:+PrintGCDateStamps \
-XX:+UnlockExperimentalVMOptions \
-XX:+UseCGroupMemoryLimitForHeap \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=./logs/ \
-XX:+UseGCLogFileRotation \
-XX:NumberOfGCLogFiles=5 \
-XX:GCLogFileSize=1M \
-Xloggc:./logs/jvm_gc.log \
-Djava.security.egd=file:/dev/./urandom"

RUN echo "https://mirrors.aliyun.com/alpine/v3.9/main" > /etc/apk/repositories \
    && echo "https://mirrors.aliyun.com/alpine/v3.9/community" >> /etc/apk/repositories \
    && apk update \
    && apk add --no-cache curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

COPY ./restful-api/build/libs/rplus-service-app-doctor*.jar rplus-service-app-doctor.jar

ENTRYPOINT java ${DEFAULT_JAVA_OPTS} ${JAVA_OPTS} -jar rplus-service-app-doctor.jar
