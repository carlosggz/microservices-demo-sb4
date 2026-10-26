FROM openjdk:25-rc-slim
RUN apt-get update && apt-get install -y curl
COPY app.jar .
ENTRYPOINT ["java","-jar","/app.jar"]