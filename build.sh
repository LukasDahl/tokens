#!/bin/bash
#title          :build.sh
#description    :This script builds the payment service
#author         :Lukas Amtoft Dahl
#==============================================================================

set -e

mvn clean package -Dquarkus.package.type=uber-jar

docker build -t tokens .
docker run -d -p 8080:8080 tokens

sleep 2s

mvn clean test