FROM eclipse-temurin:17-jre-jammy
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT java -jar -DDB_HOST=$MYSQL_HOST -DDB_PORT=$MYSQL_PORT app.jar
