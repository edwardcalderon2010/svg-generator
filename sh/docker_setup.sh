#!/bin/bash

# docker build -t com.ec/svg-generator .
# java -jar -DDB_HOST=192.188.1.2 -DDB_PORT=3306 app.jar
# java -jar -DDB_HOST=$MYSQL_HOST -DDB_PORT=$MYSQL_PORT app.jar
# docker network create --subnet 192.188.1.0/24 svg-net3
# grant ALL privileges on *.* to 'root'@'192.188.1.1' WITH GRANT OPTION;
# docker run --name=mysql23 --restart on-failure --network=svg-net3 --ip=192.188.1.2 -p 4406:3306 -d container-registry.oracle.com/mysql/community-server:8.0
# docker run --name=mysql23 --restart on-failure -v /tmp:/tmp --network=svg-net3 --ip=192.188.1.2 -p 4406:3306 -d container-registry.oracle.com/mysql/community-server:8.0
# docker run -itd --name=svg-gen-svc --network=svg-net3 --ip=192.188.1.3 -e MYSQL_HOST=192.188.1.2 -e MYSQL_PORT=3306 -p 8900:8900 com.ec/svg-generator
# docker run -itd --name=svg-web --network=svg-net3 --ip=192.188.1.4 -e SVG_SVC_HOST=192.188.1.3 -e SVG_SVC_PORT=8900 -p 8898:8898 com.ec/svg-web-front

# docker save com.ec/svg-web-front:latest | gzip > svg-web-front.tar.gz
# docker load < svg-web-front.tar.gz 