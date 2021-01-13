FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY ./target/blog.jar blog.jar
ENTRYPOINT ["java","-jar","/blog.jar", "&"]
