call mvn clean package -Dquarkus.package.type=uber-jar -Dmaven.test.skip=true
call docker build -t tokens .
call docker-compose up -d --build
timeout /t 10
call mvn test
call docker-compose down