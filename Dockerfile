FROM lsl200817-docker.pkg.coding.net/beau/common/alpine-jre8:1.0
MAINTAINER lsl <lsl200817@163.com>
WORKDIR /opt/app
VOLUME /tmp
COPY ./target/beau_web.jar beau_web.jar
ENV AVA_OPTS="-server -Xms512m -Xmx512m" \
  ENV="dev"

ENTRYPOINT java ${JAVA_OPTS} -jar -Dspring.profiles.active=${ENV} beau_web.jar
EXPOSE 7000
