FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY ./target/blog.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar", "&"]