FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/ware-manage.jar ware-manage.jar
ENTRYPOINT ["java","-jar","/ware-manage.jar", "&"]