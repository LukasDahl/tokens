#!/bin/bash
#title          :build.sh
#description    :This script builds the payment service
#author         :Lukas Amtoft Dahl
#==============================================================================

set -e

mvn clean package -Dquarkus.package.type=uber-jar

docker-compose up -d --build

sleep 2s

mvn clean test