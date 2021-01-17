#!/bin/bash
#title          :build.sh
#description    :This script builds the tokens service
#author         :Lukas Amtoft Dahl
#==============================================================================

set -e

mvn clean package -Dquarkus.package.type=uber-jar -Dmaven.test.skip=true

docker build -t tokens .

docker-compose up -d --build

sleep 2s

mvn test

docker-compose down