release: java -jar target/dependency/liquibase.jar --changeLogFile=/src/main/resources/db/master.xml --url=$JDBC_DATABASE_URL --classpath=target/dependency/postgres.jar update
web: java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port $PORT target/*.war
