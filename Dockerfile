FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/blog.jar blog.jar
ENTRYPOINT ["java","-jar","/blog.jar", "&"]