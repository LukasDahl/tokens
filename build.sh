#!/bin/bash
#title          :build.sh
#description    :This script builds the payment service
#author         :Lukas Amtoft Dahl
#==============================================================================

set -e

mvn clean package -Dquarkus.package.type=uber-jar -Dmaven.test.skip=true

docker-compose up -d --build

sleep 2s

mvn test