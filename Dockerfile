FROM openjdk:11.0-jre-slim
MAINTAINER ivan vercinsky && renny

RUN mkdir /code
COPY microservice/build/libs/*-shadow.jar /code


ENTRYPOINT [ "sh", "-c", "java -jar -Duser.timezone=$TIMEZONE -XX:MaxRAMPercentage=90 -Dnetworkaddress.cache.ttl=60 /code/*.jar" ]